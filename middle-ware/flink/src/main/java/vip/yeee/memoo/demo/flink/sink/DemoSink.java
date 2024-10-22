package vip.yeee.memoo.demo.flink.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/2 10:59
 */
@Slf4j
public class DemoSink extends RichSinkFunction<String> {

    public DemoSink() {
        log.info("DemoSink new");
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        log.info("DemoSink open");
    }

    @Override
    public void invoke(String value, Context context) throws Exception {
        log.info("DemoSink invoke ---> value = {}", value);
    }

    @Override
    public void close() throws Exception {
        log.info("DemoSink close");
    }

}
