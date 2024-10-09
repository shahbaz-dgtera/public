package sa.com.neoleap.quarkus;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@ApplicationScoped
public class DumpLogService {

    public void dumpLog(String body, String msgType) {

        try {

            JsonObject jsonObject = Json.createReader(new StringReader(body)).readObject();
            JsonObject args = jsonObject.getJsonObject("args");

            if (msgType == "AUDIT") {
                auditMsg(args);
            } else {
                dumpMsg(msgType, args, jsonObject.getString("message"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void dumpMsg(String msgType, JsonObject args, String message) {

        String appNm = args.getString("AppNm");
        String egNm = args.getString("EGNm");
        String endpoint = args.getString("Endpoint");
        String flowId = args.getString("FlowId");
        String funcId = args.getString("FuncId");
        String msgId = args.getString("MsgId");
        String partyId = args.getString("PartyId");
        String processUID = args.getString("ProcessUID");
        String rqUID = args.getString("RqUID");
        String serviceId = args.getString("ServiceId");
        String sessionId = args.getString("SessionId");
        String srvrNm = args.getString("SrvrNm");
        String tmstmp = args.getString("Tmstmp");

        StringBuilder xmlBuilder = new StringBuilder();

        xmlBuilder.append("<DumpMsg>");
        xmlBuilder.append("<MsgId>").append(msgId).append("</MsgId>");
        xmlBuilder.append("<RqUID>").append(rqUID).append("</RqUID>");
        xmlBuilder.append("<SCId>Tyk</SCId>");
        xmlBuilder.append("<FuncId>").append(funcId).append("</FuncId>");
        xmlBuilder.append("<ProcessUID>").append(processUID).append("</ProcessUID>");
        xmlBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
        xmlBuilder.append("<PartyId>").append(partyId).append("</PartyId>");
        xmlBuilder.append("<DumpMsgType>").append(msgType).append("</DumpMsgType>");
        xmlBuilder.append("<FlowId>").append(flowId).append("</FlowId>");
        xmlBuilder.append("<ServiceId>").append(serviceId).append("</ServiceId>");
        xmlBuilder.append("<AppNm>").append(appNm).append("</AppNm>");
        xmlBuilder.append("<SrvrNm>").append(srvrNm).append("</SrvrNm>");
        xmlBuilder.append("<QMGRNm>NLACEQM01D</QMGRNm>");
        xmlBuilder.append("<EGNm>").append(egNm).append("</EGNm>");
        xmlBuilder.append("<Endpoint>").append(endpoint).append("</Endpoint>");
        xmlBuilder.append("<EAIMsg>").append(message).append("</EAIMsg>");
        xmlBuilder.append("<Tmstmp>").append(tmstmp).append("</Tmstmp>");
        xmlBuilder.append("</DumpMsg>");

        String xmlStringMsg = xmlBuilder.toString();
        System.out.println("Message: " + xmlStringMsg);
        MQConnect.putToQueue(xmlStringMsg, "DumpMsgRq", msgId);
    }

    public void auditMsg(JsonObject args) {

        String msgId = args.getString("MsgId");
        String rqUID = args.getString("RqUID");
        String funcId = args.getString("FuncId");
        String partyId = args.getString("PartyId");
        String processUID = args.getString("ProcessUID");
        String sessionId = args.getString("SessionId");
        String clientDt = args.getString("ClientDt");
        String flowId = args.getString("FlowId");
        String srvcId = args.getString("SrvcId");
        String auditAction = args.getString("AuditAction");
        String bindingSrc = args.getString("BindingSrc");
        String bindingDst = args.getString("BindingDst");
        String bindingSrcHost = args.getString("BindingSrcHost");
        String tmstmp1 = args.getString("Tmstmp1");
        String tmstmp4 = args.getString("Tmstmp4");

        StringBuilder xmlBuilder = new StringBuilder();

        OffsetDateTime now = OffsetDateTime.now();

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime1 = now.format(formatter1);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTime2 = now.format(formatter2);

        // Get fractional seconds
        long nanoAdjustment = now.getNano() / 1_000; // Convert nanoseconds to microseconds
        String offset = "+00:00"; // Adjust this based on your needs

        
        xmlBuilder.append("<AuditMsg>");
        xmlBuilder.append("<MsgId>").append(msgId).append("</MsgId>");
        xmlBuilder.append("<RqUID>").append(rqUID).append("</RqUID>");
        xmlBuilder.append("<SCId>Tyk</SCId>");
        xmlBuilder.append("<FuncId>").append(funcId).append("</FuncId>");
        xmlBuilder.append("<PartyId>").append(partyId).append("</PartyId>");
        xmlBuilder.append("<ProcessUID>").append(processUID).append("</ProcessUID>");
        xmlBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
        xmlBuilder.append("<ClientDt>").append(clientDt).append("</ClientDt>");
        xmlBuilder.append("<FlowId>").append(flowId).append("</FlowId>");
        xmlBuilder.append("<SrvcId>").append(srvcId).append("</SrvcId>");
        xmlBuilder.append("<AuditAction>").append(auditAction).append("</AuditAction>");
        xmlBuilder.append("<BindngType>MQ</BindngType>");
        xmlBuilder.append("<BindingSrc>").append(bindingSrc).append("</BindingSrc>");
        xmlBuilder.append("<BindingDst>").append(bindingDst).append("</BindingDst>");
        xmlBuilder.append("<BindingSrcHost>").append(bindingSrcHost).append("</BindingSrcHost>");
        xmlBuilder.append("<AuditVars>");
        xmlBuilder.append("<Tmstmp1>").append(tmstmp1).append("</Tmstmp1>");
        xmlBuilder.append("<Tmstmp3>").append(String.format("%s.%06d%s", formattedTime2, nanoAdjustment, offset)).append("</Tmstmp3>");
        xmlBuilder.append("<Tmstmp4>").append(tmstmp4).append("</Tmstmp4>");
        xmlBuilder.append("<PutTmstmp>").append(formattedTime1).append("</PutTmstmp>");
        xmlBuilder.append("</AuditVars>");
        xmlBuilder.append("</AuditMsg>");

        String xmlStringAudit = xmlBuilder.toString();
        System.out.println("Message: " + xmlStringAudit);
        MQConnect.putToQueue(xmlStringAudit, "FlowAudngRq", msgId);
    }
}
