package com.example.redis.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
public class UserController {


    public static Logger logger=LoggerFactory.getLogger(UserController.class);


    @Autowired
    RedisTemplate<String,User> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;


    // --------------------------Key-value------------------//

    @PostMapping("/setKey")
    public void setKey(@RequestParam("key") String key,@RequestBody User value){

        logger.info("Set key {} and value {} ",key,value);
        redisTemplate.opsForValue().set(key,value);

    }
    @GetMapping("/getKey")
    public User getKey(@RequestParam("key")String key){
        logger.info("getting the value for key {} ",key);
        return redisTemplate.opsForValue().get(key);
    }

    //----------------------------------LIST Operations----------------------

    @PostMapping("/lpush")
    public void lpush(@RequestParam String key,@RequestBody User user){

        logger.info("pushing {} to left of list named by key - {}",user,key);
        redisTemplate.opsForList().leftPush(key,user);

    }
    @PostMapping("/rpush")
    public void rpush(@RequestParam String key, @RequestBody User user){
        logger.info("pushing {} to right of list named by key - {}",user,key);
        redisTemplate.opsForList().rightPush(key,user);
    }
    @PostMapping("/lpop")
    public void lpop(@RequestParam String key){
        logger.info("popping item from left side of list {} ",key);
        redisTemplate.opsForList().leftPop(key);
    }
    @PostMapping("/rpop")
    public void rpop(@RequestParam String key){
        logger.info("popping item from right side of list {} ",key);
        redisTemplate.opsForList().rightPop(key);
    }
    @GetMapping("/lrange")
    public List<User> lrange(@RequestParam String key, @RequestParam int start, @RequestParam int end){
        List<User> userList=redisTemplate.opsForList().range(key,start,end);
        return userList;
    }

    //---------------------------- Hash Map Operations----------------------------------

    @PostMapping("/hmset")
    public void hmset(@RequestParam String key,@RequestBody User user){
        Map<String,String>map=objectMapper.convertValue(user,Map.class);
        redisTemplate.opsForHash().putAll(key,map);
    }
    @GetMapping("/hmget")
    public User hmget(@RequestParam String key){
        Map map=redisTemplate.opsForHash().entries(key);
        return objectMapper.convertValue(map,User.class);
    }



}
