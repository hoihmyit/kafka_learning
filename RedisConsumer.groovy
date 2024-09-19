@Grapes(
        @Grab(group='redis.clients', module='jedis', version='5.0.1')
)
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

def channel = "refresh_config";
// Create a new subscriber
JedisPubSub subscriber = new JedisPubSub() {
    @Override
    public void onMessage(String ch, String message) {
        // Handle incoming messages
        System.out.println("Received: " + message);
    }
};
JedisPool pool = new JedisPool("localhost", 6379);
Jedis jedis = pool.getResource();
// Subscribe to the channel
jedis.subscribe(subscriber, channel);