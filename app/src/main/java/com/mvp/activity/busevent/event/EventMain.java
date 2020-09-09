package com.mvp.activity.busevent.event;


/**
 * 事件消息
 */
public class EventMain {
    //具体事件发送的消息
    private String messgae;
//    private StudyBean studyBean;
//
//    public StudyBean getStudyBean() {
//        return studyBean;
//    }
//
//    public void setStudyBean(StudyBean studyBean) {
//        this.studyBean = studyBean;
//    }
//
    public EventMain(String messgae/*, StudyBean studyBean*/) {
        this.messgae = messgae;
      //  this.studyBean = studyBean;
    }

    public String getMessgae() {
        return messgae;
    }

    public static class FirstEventMain {
        private static final String TAG = "FirstEventMain";
    }
}
