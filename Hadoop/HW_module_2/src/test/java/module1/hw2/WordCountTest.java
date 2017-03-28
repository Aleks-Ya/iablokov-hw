package module1.hw2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class WordCountTest {
    @Test
    public void test() throws IOException {
        new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>()
                .withReducer(new SumReducer())
                .withMapper(new StringToWordMapper())
                .withInput(new LongWritable(0), new Text("a b c d a b a"))
                .withAllOutput(
                        Arrays.asList(
                                new Pair<>(new Text("a"), new IntWritable(3)),
                                new Pair<>(new Text("b"), new IntWritable(2)),
                                new Pair<>(new Text("c"), new IntWritable(1)),
                                new Pair<>(new Text("d"), new IntWritable(1))
                        ))
                .runTest();
    }
}
