package cn.syf.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
   private static  Jedis jedis =null;


    public static Jedis getRedis(){
       if(jedis==null){
           JedisPoolConfig config=new JedisPoolConfig();
           config.setMaxIdle(5);
           config.setMinIdle(5);
           config.setMaxTotal(10);
           config.setMaxWaitMillis(3000);

           JedisPool jedisPool = new JedisPool(config, "spring", 6379);
           jedis=jedisPool.getResource();
       }
       return jedis;
    }
}
