package sa.com.neoleap.quarkus;

import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQGetMessageOptions;

public class MQConnect {
    
    public static void putToQueue(String payload, String queueName, String msgId)
    {
        MQQueueManager qMgr = null;
        MQQueue queue = null;

        // queueName = "SYSTEM.DEFAULT.LOCAL.QUEUE";
        // payload = "";
        // int openOptions = CMQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_BROWSE | MQC.MQOO_INQUIRE | CMQC.MQOO_OUTPUT;
        
        int openOptions = CMQC.MQOO_FAIL_IF_QUIESCING| CMQC.MQOO_OUTPUT;
        try
        {

            MQEnvironment.hostname = "";
            MQEnvironment.channel = "";
            MQEnvironment.port = 1234;
            
            qMgr = new MQQueueManager(null);
            queue = qMgr.accessQueue(queueName, openOptions);
            
            // int depth=queue.getCurrentDepth();
            // System.out.println("Message depth: " + depth);

            MQMessage mqMsg = new MQMessage();
            mqMsg.writeString(payload);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            queue.put(mqMsg, pmo);
            System.out.println("Message put success: " + msgId);
            
            // MQMessage mqGetMsg = new MQMessage();
            // //assign message id to get message.
            // mqGetMsg.messageId = mqMsg.messageId;
            
            // //get message options.
            // MQGetMessageOptions gmo = new MQGetMessageOptions();
            // // gmo.waitInterval = MQC.MQWI_UNLIMITED; // or a specific time in milliseconds
            // queue.get(mqGetMsg, gmo);

            // String readMsg = mqGetMsg.readStringOfByteLength(mqGetMsg.getMessageLength());
            // System.out.println("Message got from MQ: " + readMsg);
        }
        catch(Exception x)
        {
            x.printStackTrace();
        }
        finally
        {
            try
            {
                queue.close();
                qMgr.disconnect();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
 