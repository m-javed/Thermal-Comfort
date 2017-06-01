library(kernlab); library(caret);
library(e1071); library(ggplot2);
library(memisc)

#-------------------------Loading Data-----------------------------
data_filename <- 'DataJaved919 - Copy'
data_filename_old <- paste(data_filename,'-old', sep='')
data_filename <- paste(data_filename,'.txt',sep='')
data_filename_old <- paste(data_filename_old,'.txt',sep='') #file name for saving old data

readdata <- read.delim(data_filename, header=FALSE, stringsAsFactors=FALSE)
readdata_old <- read.delim(data_filename_old, header=FALSE, stringsAsFactors=FALSE)
mydata <- data.frame(readdata[,c(5,4,3)])  #extract column 3, 4 and 5
mydata_old <- data.frame(readdata_old[,c(5,4,3)])  #extract column 3, 4 and 5

mydata[,3] <- -(mydata[,3])
mydata_old[,3] <- -(mydata_old[,3])
col_headings <- c('RH','Temperature','Response') #names for data type
names(mydata) <- col_headings #header of data frame
names(mydata_old) <- col_headings #header of data frame
#write data to the file for future use
#write.table(readdata,data_filename_old,quote=FALSE,sep='\t',row.names=FALSE,col.names=FALSE)
#------------------------------------------------------------------
mydata_old$Response <- factor(mydata_old$Response)
mydata$Response <- factor(mydata$Response)
data_length <- nrow(mydata_old)
mydata_new <- tail(mydata,nrow(mydata)-data_length)
if(nrow(mydata_new)<=1){
  data_new_length <- nrow(mydata_new)
}else{
  data_new_length <- seq(1,nrow(mydata_new))
}

if(nrow(mydata_new)>0){ #if we have a new data
  #-----------------------------------------------------Training model on old data-------------------------
  for(i in data_new_length){
    #print("this is for loop")
    
    #training <- mydata_old
    inTrain <- createDataPartition(y = mydata$Response, 
                                   p=0.70, list=FALSE);
    training <- mydata[inTrain,];
    #testing1 <- mydata[-inTrain,];
    
    writeLines("Tuning the model...")
    #my_model <- svm(Response~., data=training,cost=1,gamma=0.5)#, type="nu-classification", scale=TRUE, nu=1) #, cost=200, gamma=1)
    tune_model <- tune(svm, Response~.,data=mydata_old,ranges=
                         list(cost=c(0.01,0.1,1,10,100,1000),gamma=c(0.125,0.25,0.375,0.5,0.625,0.75)))
    
    final_model <- tune_model$best.model
    
    my_predict <- predict(final_model,mydata_new[i,])
    #result <- confusionMatrix(mydata_new[i,]$Response,my_predict)
    #accu <- unname(result$overall[1],force = FALSE) #remove the name 'accuracy'
    
    if(data_length>100){
      if(my_predict == mydata_new[i,]$Response){
        writeLines("New data was classified correctly. The old model is working fine! :D\n")
      } else{
        writeLines("Classified incorrectly!\n")
        
        sv_index <- final_model$index
        sv <- subset(mydata_old[sv_index,],Response==mydata_new[i,]$Response)#get sv with same class as new data
        non_sv <- subset(mydata_old[-sv_index,],Response==mydata_new[i,]$Response)#get non-sv with same class as new data
        dist <- 0
        max_index <- strtoi(row.names(non_sv)[1],0L)
        for(j in seq(1,nrow(sv))){#loop over sv
          for(k in seq(1,nrow(non_sv))){#loop over non-sv
            x1 <- sv[j,]$Temperature
            x2 <- non_sv[k,]$Temperature
            y1 <- sv[j,]$RH
            y2 <- non_sv[k,]$RH
            d <- (x2-x1)^2+(y2-y1)^2#distance between a sv and non-sv
            if(d>dist){
              max_index <- strtoi(row.names(non_sv[k,])[1],0L)
              dist <- d
            }
          }
        }
        #add the new data point
        writeLines(paste("Added data point: \n\tTemp:",mydata_new[i,]$Temperature,"\n\tRH:",mydata_new[i,]$RH,"\n\tClass:",mydata_new[i,]$Response))
        readdata_old[max_index,] <- readdata[data_length+i,]
        #remove the old data point
        writeLines(paste("\nRemoved data point: \n\tTemp:",mydata_old[max_index,]$Temperature,"\n\tRH:",mydata_old[max_index,]$RH,"\n\tClass:",mydata_old[max_index,]$Response))
        
        }
    }
  }
}

write.table(readdata_old,data_filename_old,quote=FALSE,sep='\t',row.names=FALSE,col.names=FALSE)
write.table(readdata_old,data_filename,quote=FALSE,sep='\t',row.names=FALSE,col.names=FALSE)
