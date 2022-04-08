package cn.sliew.http.stream.akka.page;

import akka.stream.javadsl.Source;
import cn.sliew.http.stream.remote.jst.JstRemoteService;

import java.util.concurrent.CompletionStage;

public class JstOrderSource {

    private final JstRemoteService jstRemoteService;

    public JstOrderSource(JstRemoteService jstRemoteService) {
        this.jstRemoteService = jstRemoteService;
    }

    public void process() {
        JstOrderClient jstOrderClient = new JstOrderClient(jstRemoteService);
        CompletionStage<JstOrderPage> completionStage = jstOrderClient.fetchPage(null);
//        Source.repeat()
//        Source.completionStage(completionStage)


    }
}
