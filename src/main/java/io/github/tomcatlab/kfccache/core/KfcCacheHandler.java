package io.github.tomcatlab.kfccache.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.StringUtils;

public class KfcCacheHandler extends SimpleChannelInboundHandler<String> {
    private static final String CRLF = "\r\n";
    private static final String OK = "OK";
    private static final String STR_PREFIX = "+";
    private static final String BULK_PREFIX = "$";
//    private static final String INFO = "KFCCache server[v1.0] created by cola."+CRLF+
//            "mock redis server, at 2024-06-12 in Beijing."+CRLF;
    private static final KfcCache CACHE = new KfcCache();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String redisMessage) throws Exception {
//        if (redisMessage instanceof ArrayHeaderRedisMessage m){
//            System.out.println("1 =>"+m.length());
//        }
//
//        if (redisMessage instanceof BulkStringHeaderRedisMessage m){
//            System.out.println("2 =>"+m.bulkStringLength());
//        }
//
//        if (redisMessage instanceof DefaultBulkStringRedisContent m){
//            int count = m.content().readableBytes();
//            byte[] bytes = new byte[count];
//            m.content().readBytes(bytes);
//
//            System.out.println("3 =>"+new String(bytes));
//            ctx.writeAndFlush("+OK\r\n");
//        }

        String[] args = redisMessage.split(CRLF);
        System.out.println("redisMessage:"+String.join(",",args));
        String cmd = args[2].toUpperCase();
        Command command = Commands.get(cmd);
        if (command!=null){
            try {
                Reply<?> reply = command.execute(CACHE, args);
                System.out.println("CMD[" + cmd + "] => " + reply.type + " => " + reply.value);
                replyContext(ctx, reply);
            }catch (Exception e){
                Reply<String> reply = Reply.error("ERR  command '" + cmd + "'" + "msg: " + e.getMessage());
                replyContext(ctx, reply);
            }

        }else  {
            Reply<?> reply = Reply.error("ERR unsupported command '" + cmd + "'");
            replyContext(ctx, reply);
        }
//        if ("COMMAND".equals(cmd)){
//            writeByteBuf(ctx,"*2"
//                    +CRLF+"$7"
//                    +CRLF+"COMMAND"
//                    +CRLF+"$4"
//                    +CRLF+"PING"
//                    +CRLF);
//            return;
//        }

//        if ("PING".equals(cmd)){
//            String res ="PONG";
//            if (args.length>=5) {
//                 res = args[4];
//            }
//            simpleString(ctx,res);
//            return;
//        }

//        if ("INFO".equals(cmd)){
//            bulkString(ctx,INFO);
//            return;
//        }

//        if ("SET".equals(cmd)){
//            String key = args[4];
//            String length = args[5];
//            if ("$0".equals(length)){
//                CACHE.set(key,"");
//            } else  {
//                CACHE.set(key,args[6]);
//            }
//            simpleString(ctx,OK);
//            return;
//        }

//        if ("GET".equals(cmd)){
//           String val = CACHE.get(args[4]);
//           if (StringUtils.isEmpty(val)){
//               simpleString(ctx,val);
//           }else{
//               bulkString(ctx,val);
//           }
//            return;
//        } else if ("STRLEN".equals(cmd)){
//            String val = CACHE.get(args[4]);
//            integerBulkString(ctx,val==null?0:val.length());
//            return;

//        }else if ("DEL".equals(cmd)){
//            String[] keys = new String[(args.length-3)/2];
//            for (int i = 0; i < keys.length; i++) {
//                keys[i] = args[4+i*2];
//            }
//            long del = CACHE.del(keys);
//            integerBulkString(ctx,del);
//            return;

//        }else if ("EXISTS".equals(cmd)){
//            String[] keys = new String[(args.length-3)/2];
//            for (int i = 0; i < keys.length; i++) {
//                keys[i] = args[4+i*2];
//            }
//            long exists = CACHE.exists(keys);
//            integerBulkString(ctx,exists);
//            return;

//        }else if ("MGET".equals(cmd)){
//            String[] keys = new String[(args.length-3)/2];
//            for (int i = 0; i < keys.length; i++) {
//                keys[i] = args[4+i*2];
//            }
//            String[] results = CACHE.mget(keys);
//            array(ctx,results);
//            return;
//
//        }else if ("MSET".equals(cmd)){
//            String[] keys = new String[(args.length-3)/4];
//            String[] vals = new String[(args.length-3)/4];
//            for (int i = 0; i < keys.length; i++) {
//                keys[i] = args[4+i*4];
//                vals[i] = args[6+i*4];
//            }
//            CACHE.mset(keys,vals);
//            simpleString(ctx,OK);
//            return;
//
//        } else if ("DECR".equals(cmd)) {
//            try {
//                long val = CACHE.decr(args[4]);
//                integerBulkString(ctx,val);
//            }catch (NumberFormatException e){
//                error(ctx, "NFE " + args[4] + " value[" + CACHE.get(args[4]) + "] is not an integer.");
//            }
//            return;
//
//        }else if ("INCR".equals(cmd)) {
//            try {
//                long val = CACHE.incr(args[4]);
//                integerBulkString(ctx,val);
//            }catch (NumberFormatException e){
//                error(ctx, "NFE " + args[4] + " value[" + CACHE.get(args[4]) + "] is not an integer.");
//
//            }
//            return;
//        }
////        ctx.writeAndFlush("+OK\r\n");
//        simpleString(ctx,OK);
    }

    private void replyContext(ChannelHandlerContext ctx, Reply<?> reply) {
        switch (reply.type){
            case INT:
                integerBulkString(ctx, (int) reply.value);
                break;
            case SIMPLE_STRING:
                simpleString(ctx, (String) reply.value);
                break;
            case ERROR:
                error(ctx, (String) reply.value);
                break;
            case ARRAY:
                array(ctx, (String[]) reply.value);
                break;
            case BULK_STRING:
                bulkString(ctx, (String) reply.value);
                break;
                default:
                    throw new RuntimeException("unsupported reply type:"+reply.type);
        }
    }

    private void error(ChannelHandlerContext ctx,String msg) {
        writeByteBuf(ctx,errorEncode(msg));
    }

    private String errorEncode( String msg) {
        return "-"+ msg + CRLF;
    }

    private void integerBulkString(ChannelHandlerContext ctx, int i) {
        writeByteBuf(ctx,integerBulkStringEncode(i));
    }

    private String integerBulkStringEncode( long i) {
        return ":"+ i + CRLF;
    }
    private String arraygEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if (array == null){
            sb.append("*-1"+CRLF);
        }else if (array.length == 0){
            sb.append("*0"+CRLF);
        }else {
            sb.append("*"+array.length+CRLF);
            for (int i = 0; i < array.length; i++) {
                Object o = array[i];
                if (o == null){
                    sb.append("$-1"+CRLF);
                }else {
                    if (o instanceof String){
                        sb.append(bulkStringEncode((String) o));
                    }else if (o instanceof Long){
                        sb.append(integerBulkStringEncode((Long) o));
                    }else if (o instanceof Object[] obj){
                        sb.append(arraygEncode(obj));
                    }
                }
            }

        }
        return sb.toString();
    }
    private void array(ChannelHandlerContext ctx, String[] array){

        writeByteBuf(ctx,arraygEncode(array));
    }
    private void writeByteBuf(ChannelHandlerContext ctx, String content){
        System.out.println("wrap byte buffer:"+content);
        ByteBuf buffer = Unpooled.buffer(128);
        buffer.writeBytes(content.getBytes());
        ctx.writeAndFlush(buffer);
    }

    private void simpleString(ChannelHandlerContext ctx, String content){
        writeByteBuf(ctx,simpleStringEncode(content));
    }

    private void bulkString(ChannelHandlerContext ctx, String content){
        writeByteBuf(ctx,bulkStringEncode(content));
    }

    private static String bulkStringEncode(String content) {
        String res;
        if (content == null){
            res = "$-1"+CRLF;
        }else if (content.isEmpty()){
            res = "$0"+CRLF;
        }else {
            res = BULK_PREFIX+ content.getBytes().length+CRLF+ content +CRLF;
        }
        return res;
    }

    private String simpleStringEncode(String content){
        String res =content;
        if (content == null){
            res = "$-1";
        }else if (content.isEmpty()){
            res = "$0"+CRLF;
        }else {
            res = STR_PREFIX+res;
        }
       return res+CRLF;
    }
}
