library("ggplot2")
# Input File path
inputFile = "csvStat.csv"

# Read input file from CSV-format
file1<-read.csv(inputFile)

# Output File path
outputFile = "report.txt"

# Output Figure path
outputFigure = "report.pdf"

# Create output file object
out<-file(outputFile)

# Create vector of values
# Many need to change "2" if values are in a different column
x<-as.double(file1[[4]])

# Create data frame
df1<-data.frame(group="GooglePlay APKs" , values=x)

# Write descriptive statistics
writeLines(summary(df1),out)

# Create plot and remove outliers
# plot will append mean value
plot <-
    ggplot(df1, aes(x=group, y=values, fill = group)) + geom_boxplot(outlier.shape = NA ) + stat_summary(
      fun = base::mean,
      color = "black",
      geom = "point" ,
      aes(group = 1),
      size = 5,
      show.legend = FALSE
    ) + theme(axis.title.x = element_blank(),axis.title.y = element_text(),
          legend.position = "none", text = element_text(size = 30)) + labs( y = "% Elements with Asstistive Text")
 
# compute lower and upper whiskers
ylim1 = boxplot.stats(df1$values)$stats[c(5)]
  

# scale y limits based on ylim1
p1 = plot + coord_cartesian(ylim = c(0,ylim1*1.05))
  
# Save figure
ggsave(outputFigure, plot = last_plot(), device = "pdf", path = ".",
         scale = 1, width = NA, height = NA, units = c("in", "cm", "mm"),
         dpi = 300, limitsize = FALSE )

