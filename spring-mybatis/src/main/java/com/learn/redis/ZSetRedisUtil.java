package com.learn.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * redisTemplate 操作 ZSET 有序集合， 参考网站：http://www.redis.cn/commands.html
 */
@Component
public class ZSetRedisUtil {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * ZADD [KEY] [SCORE1] [MEMBER1] [SCORE2] [MEMBER2]......
     * 将所有指定成员添加到键为key有序集合（sorted set）里面。 添加时可以指定多个分数/成员（score/member）对。
     * 如果指定添加的成员已经是有序集合里面的成员，则会更新改成员的分数（scrore）并更新到正确的排序位置。
     * @param key
     * @param value
     * @param score
     * @return boolean
     */
    public boolean zadd(String key, Object value, double score) {
        Boolean add = redisTemplate.boundZSetOps(key).add(value, score);
        return Boolean.TRUE.equals(add);
    }

    /**
     * ZADD [KEY] [NX|XX] [CH] [INCR] [SCORE1] [MEMBER1] [SCORE2] [MEMBER2]......
     * 将所有指定成员添加到键为key有序集合（sorted set）里面。 添加时可以指定多个分数/成员（score/member）对。
     * 如果指定添加的成员已经是有序集合里面的成员，则会更新改成员的分数（scrore）并更新到正确的排序位置。
     *
     * XX: 仅仅更新存在的成员，不添加新成员。
     * NX: 不更新存在的成员。只添加新成员。
     * CH: 修改返回值为发生变化的成员总数，原始是返回新添加成员的总数 (CH 是 changed 的意思)。
     *     更改的元素是新添加的成员，已经存在的成员更新分数。 所以在命令中指定的成员有相同的分数将不被计算在内。
     *     注：在通常情况下，ZADD返回值只计算新添加成员的数量。
     * INCR: 当ZADD指定这个选项时，成员的操作就等同ZINCRBY命令，对成员的分数进行递增操作。
     *
     * @param key
     * @param valueMap
     * @return boolean 新增>0 返回 true, 只是更新的话返回值=0, 也认为是true
     */
    public boolean zadd(String key, Map<Object, Double> valueMap) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        for (Map.Entry<Object, Double> entry : valueMap.entrySet()) {
            ZSetOperations.TypedTuple<Object> tuple = new DefaultTypedTuple<>(entry.getKey(), entry.getValue());
            tuples.add(tuple);
        }

        Long add = redisTemplate.boundZSetOps(key).add(tuples);
        return Objects.nonNull(add) && add >= 0;
    }

    /**
     * ZRANGE [KEY] [NX|XX] [CH] [INCR] [START] [END]
     * 返回有序集key中成员member的排名。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，score值最小的成员排名为0
     *
     * XX: 仅仅更新存在的成员，不添加新成员。
     * NX: 不更新存在的成员。只添加新成员。
     * CH: 修改返回值为发生变化的成员总数，原始是返回新添加成员的总数 (CH 是 changed 的意思)。
     *     更改的元素是新添加的成员，已经存在的成员更新分数。 所以在命令中指定的成员有相同的分数将不被计算在内。
     *     注：在通常情况下，ZADD返回值只计算新添加成员的数量。
     * INCR: 当ZADD指定这个选项时，成员的操作就等同ZINCRBY命令，对成员的分数进行递增操作。
     *
     * @param key
     * @param start
     * @param end
     * @return Set
     */
    public Set<Object> zrange(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).range(start, end);
    }

    /**
     * ZRANGE [KEY] [START] [END] WITHSCORES
     * 返回有序集key中成员member和对应分数的排名。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，score值最小的成员排名为0
     * @param key
     * @param start
     * @param end
     * @return List<Map<Object, Double>>
     */
    public List<Map<Object, Double>> zrangeWithScore(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.boundZSetOps(key).rangeWithScores(start, end);
        if(Objects.isNull(typedTuples)) {
            return null;
        }
        List<Map<Object, Double>> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<Object> tuple : typedTuples) {
            Map<Object, Double> member = new HashMap<>();
            member.put(tuple.getValue(), tuple.getScore());
            list.add(member);
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * ZRANGEBYLEX [KEY] [MIN|(MIN [MAX|(MAX [LIMIT offset count]
     * exp: zrangebylex zset ["\"aa\"" ["\"c\"" limit 1 3
     * 返回指定成员区间内的成员，按成员字典正序排序, 分数必须相同.
     * LIMIT	否	返回结果是否分页,指令中包含LIMIT后offset、count必须输入
     * offset	否	返回结果起始位置
     * count	否	返回结果数量
     * @param key
     * @param min
     * @param includeMin 是否包含边界 true: min <= ? false: min < ?
     * @param max
     * @param includeMax  是否包含边界 true: ? <= max; false: ? < max
     * @return Set
     */
    public Set<Object> zrangeByLex(String key, Object min, boolean includeMin, Object max, boolean includeMax) {
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        // min<? | min<=?
        if(includeMin) {
            range.gte(Objects.requireNonNull(serializer().serialize(min)));
        }else {
            range.gt(Objects.requireNonNull(serializer().serialize(min)));
        }
        // ?<max | ?<=max
        if(includeMax) {
            range.lte(Objects.requireNonNull(serializer().serialize(max)));
        }else {
            range.lt(Objects.requireNonNull(serializer().serialize(max)));
        }

        return redisTemplate.boundZSetOps(key).rangeByLex(range);
    }

    /**
     * ZRANGEBYSCORE [KEY] [MIN] [MAX] [LIMIT offset count] 默认包含边界，不想包含边界使用 (MIN (MAX
     * 返回key的有序集合中的分数在min和max之间的所有元素（包括分数等于max或者min的元素）。元素被认为是从低分到高分排序的。
     * 具有相同分数的元素按字典序排列（这个根据redis对有序集合实现的情况而定，并不需要进一步计算）。
     * @param key
     * @param min
     * @param max
     * @return Set
     */
    public Set<Object> zrangeByScore(String key, double min, double max) {
         return redisTemplate.boundZSetOps(key).rangeByScore(min, max);
    }

    /**
     * ZRANGEBYSCORE [KEY] [MIN] [MAX] WITHSCORES [LIMIT offset count] 默认包含边界，不想包含边界使用 (MIN (MAX
     * 返回有序集key中成员member和对应分数的排名。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，score值最小的成员排名为0
     * @param key
     * @param min
     * @param max
     * @return List<Map<Object, Double>>
     */
    public List<Map<Object, Double>> zrangeByScoreWithScore(String key, double min, double max){
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.boundZSetOps(key).rangeByScoreWithScores(min, max);
        if(Objects.isNull(typedTuples)) {
            return null;
        }
        List<Map<Object, Double>> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<Object> tuple : typedTuples) {
            Map<Object, Double> member = new HashMap<>();
            member.put(tuple.getValue(), tuple.getScore());
            list.add(member);
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * ZRANK [KEY] [MEMBER KEY]
     * 返回有序集key中成员member的排名。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，score值最小的成员排名为0。
     * @param key
     * @param memberKey
     * @return long
     */
    public long zrank(String key, Object memberKey) {
        Long rank = redisTemplate.boundZSetOps(key).rank(Objects.requireNonNull(serializer().serialize(memberKey)));
        return Objects.nonNull(rank) ? rank : -1;
    }

    /**
     * ZREM [KEY] [MEMBER KEY]......
     * 返回的是从有序集合中删除的成员个数，不包括不存在的成员. 当key存在，但是其不是有序集合类型，就返回一个错误
     * @param key
     * @param memberKey
     * @throws
     * @return 删除个数
     */
    public long zrem(String key, Object... memberKey) {
        Long remove = redisTemplate.boundZSetOps(key).remove((Object) serializer().serialize(memberKey));
        return Objects.nonNull(remove) ? remove : 0;
    }

    /**
     * ZCARD [KEY]
     * key存在的时候，返回有序集的元素个数，否则返回0。
     * @param key
     * @return
     */
    public long zcard(String key) {
        Long size = redisTemplate.boundZSetOps(key).size();
        return Objects.nonNull(size) ? size : 0;
    }

    /**
     * ZCOUNT [KEY] [MIN] [MAX]
     * 返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员个数, 默认包含边界。想要不包含边界使用 (MIN (MAX
     * @param key
     * @param min
     * @param max
     * @return 成员个数
     */
    public long zcount(String key, double min, double max) {
        Long count = redisTemplate.boundZSetOps(key).count(min, max);
        return Objects.nonNull(count) ? count : 0;
    }

    /**
     * ZSCORE [KEY] [MEMBER KEY]
     * 返回有序集key中，成员member的score值。如果member元素不是有序集key的成员，或key不存在，返回nil。
     * @param key
     * @param memberKey
     * @return 不存在返回 null
     */
    public Double zscore(String key, Object memberKey) {
        return redisTemplate.boundZSetOps(key).score(Objects.requireNonNull(serializer().serialize(memberKey)));
    }

    /**
     * ZINCRBY [KEY] [INCREMENT] [MEMEBER KEY]
     * 为有序集key的成员member的score值加上增量increment。如果key中不存在member，就在key中添加一个member，
     * score是increment（就好像它之前的score是0.0）。如果key不存在，就创建一个只含有指定member成员的有序集合。
     * 当key不是有序集类型时，返回一个错误。
     * @param key
     * @param memberKey
     * @param increment 加数
     * @return
     */
    public Double zincrby(String key, double increment, Object memberKey) {
        return redisTemplate.boundZSetOps(key).incrementScore(Objects.requireNonNull(serializer().serialize(memberKey)), increment);
    }

    /**
     * 成员键的序列化类
     * @return Jackson2JsonRedisSerializer
     */
    private static Jackson2JsonRedisSerializer<Object> serializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

}
