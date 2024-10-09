package sa.com.neoleap.quarkus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/dumplog")
public class DumpLogResource {

    @Inject
    DumpLogService service;

    @POST
    @Path("/{block}")
    public Response dumpLog(String body, String block) {

        // System.out.println("REQ:\n" + body);
        if (body == null || body.isEmpty()) {
            System.out.println("Middleware block: Err[" + block + "], Body: Empty!");
            return Response.ok("body is empty").build();
        } else {
            Pattern pattern = Pattern.compile("\"MsgId\":\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(body);
            String mID = matcher.find() ? matcher.group(1) : null;

            if (block.equalsIgnoreCase("client-request")) {
                System.out.println("Middleware block: " + block + "\nFrom: Pre Plugin\n Message ID [" + mID + "]");
                service.dumpLog(body, "REQ");
            } else if (block.equalsIgnoreCase("upstream-pre-request")) {
                System.out.println("Middleware block: " + block + "\nFrom: Post Plugin\n Message ID [" + mID + "]");
                service.dumpLog(body, "IREQ");
            } else if (block.equalsIgnoreCase("upstream-request")) {
                System.out.println("Middleware block: " + block + "\nFrom: Analytics Plugin\n Message ID [" + mID + "]");
                service.dumpLog(body, "IREQ");
            } else if (block.equalsIgnoreCase("upstream-response")) {
                System.out.println("Middleware block: " + block + "\nFrom: Response Plugin\n Message ID [" + mID + "]");
                service.dumpLog(body, "IRPLY");
            } else if (block.equalsIgnoreCase("client-response")) {
                System.out.println("Middleware block: " + block + "\nFrom: Analytics Plugin\n Message ID [" + mID + "]");
                service.dumpLog(body, "RPLY");
            } else if (block.equalsIgnoreCase("audit")) {
                System.out.println("Middleware block: " + block + "\nFrom: Analytics Plugin\n Message ID [" + mID + "]");
                service.dumpLog(body, "AUDIT");
            } else {
                System.out.println("Middleware block: Err[" + block + "]");
                return Response.ok("invalid endpoint").build();
            }
        }
        return Response.ok("success").build();
    }

}