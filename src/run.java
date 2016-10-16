/**
 * This is the Assignment 1b of 10605
 * Main Function of MapReduce Training of Naive Bayes
 * @author Hao Wang (haow2)
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class run {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        Job job = new Job(conf, "NB");
        job.setJarByClass(NB_train_hadoop.class);

        // Set Mapper and Reducer
        job.setMapperClass(NB_train_hadoop.NBMapper.class);
        job.setReducerClass(NB_train_hadoop.NBReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        if (args.length == 3) {
        	job.setNumReduceTasks(Integer.parseInt(args[2]));
        }

        job.waitForCompletion(true);
    }
}