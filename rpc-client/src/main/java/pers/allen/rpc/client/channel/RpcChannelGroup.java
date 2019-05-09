package pers.allen.rpc.client.channel;

import io.netty.channel.Channel;
import pers.allen.rpc.server.dto.RequestMsg;
import pers.allen.rpc.server.dto.RequestMsgBuilder;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RpcChannelGroup {

    /**
     * InetSocketAddress Override (equals and hahsCode)
     */
   protected Map<SocketAddress, Channel> channelMap = new ConcurrentHashMap<>();

   protected abstract Channel getChannel(String serviceName);
   public abstract void remoteRequest(RequestMsg msg);

    public boolean remove(SocketAddress key) {
         Channel channel = null;
         channel = channelMap.remove(key);
         if(channel == null) {
            return false;
         }
         return true;
   }

   public void removeChannel(Object o) {
      if(o instanceof  Channel) {
         if(channelMap.containsValue(o)) {
            Collection<Channel> channels = channelMap.values();
            channels.remove(o);
         }
      }
   }

}
