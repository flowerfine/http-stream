package cn.sliew.http.stream.akka.framework.jst;

import cn.sliew.http.stream.akka.framework.SubTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JstSubTask implements SubTask {

    private Long identifier;

    private Date startTime;

    private Date endTime;

    @Override
    public Long getIdentifier() {
        return identifier;
    }

}
