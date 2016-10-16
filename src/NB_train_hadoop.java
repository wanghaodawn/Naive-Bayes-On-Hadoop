/**
 * This is the Assignment 1b of 10605
 * Mapper and Reducer of Naive Bayes Training
 * @author Hao Wang (haow2)
 */
import java.io.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class NB_train_hadoop {
	/*
	 * The mapper for Naive Bayes
	 */
	public static class NBMapper extends Mapper<Object, Text, Text, IntWritable> {

	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();

	    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

	      	// Parse the string
	        String currLine = value.toString().trim();

	        String[] ss = currLine.split("\t");
            if (ss.length != 3) {
            	return;
            }

            String[] labels = ss[1].trim().split(",");
            // Traverse all labels
            for (int i = 0; i < labels.length; i++) {
            	String label = labels[i];
            	if (label.length() == 0) {
            		continue;
            	}
            	String strY = "Y=" + label;

        		// Check unique class
            	
            	// Increment #(Y=y) by 1
            	word.set(strY);
            	context.write(word, one);

            	// Increment #(Y=*) by 1
            	word.set("Y=*");
            	context.write(word, one);

            	// Words Tokenizer
            	String[] words = ss[2].trim().split("\\s+");
            	int countWord = 0;
            	for (int j = 0; j < words.length; j++) {
            		words[j] = words[j].replaceAll("\\W", "");
            		if (words[j].length() == 0) {
            			continue;
            		}

            		// Increment #(Y=y,W=wj) by 1
            		word.set(strY + ",W=" + words[j]);
            		context.write(word, one);
        			countWord++;
            	}

            	// Increment #(Y=y,W=*) by countWord
            	word.set(strY + ",W=*");
            	context.write(word, new IntWritable(countWord));
            }
	    }
	}
	/*
	 * The reducer for Naive Bayes
	 */
	public static class NBReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}
}