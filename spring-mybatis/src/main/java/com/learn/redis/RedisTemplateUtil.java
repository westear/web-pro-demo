package com.learn.redis;

import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.commands.MultiKeyCommands;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 参考网站：http://www.redis.cn/commands.html
 */
@Component
public class RedisTemplateUtil {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * key 相关操作
     */

    /**
     * TYPE [KEY]
     * 判断值类型
     * @param key 键名
     * @return DataType enum
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 判断键是否存在
     * EXISTS [KEY]
     * @param keys key list
     * @return
     */
    public Long existsKey(List<String> keys) {
        return redisTemplate.countExistingKeys(keys);
    }

    public boolean expireKey(String key, Long time, TimeUnit timeUnit) {
        Boolean expire = redisTemplate.expire(key, time, timeUnit);
        return Boolean.TRUE.equals(expire);
    }

    public Long ttlKey(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 为指定key的值加一（整数 + 1）
     * INCR [KEY]
     * @param key
     * @return Long
     */
    public Long incr(String key) {
        return redisTemplate.boundValueOps(key).increment();
    }

    /**
     * 为指定key的值增加指定 int 类型数值（整数 + 整数）
     * INCRBY [KEY]
     * @param key
     * @return Long
     */
    public Long incrBy(String key, long step) {
        return redisTemplate.boundValueOps(key).increment(step);
    }

    /**
     * 为指定key的值增加指定 double 类型数值（小数 + 小数）
     * INCRBYFLOAT [KEY]
     * @param key
     * @return Double
     */
    public Double incrBy(String key, double step) {
        return redisTemplate.boundValueOps(key).increment(step);
    }

    /**
     * 为指定key的值减少 int 类型数值（整数 - 1）
     * DECR [KEY]
     * @param key
     * @return Long
     */
    public Long decr(String key) {
        return redisTemplate.boundValueOps(key).decrement();
    }
    /**
     * 为指定key的值减少指定 int 类型数值（整数 - 整数）
     * DECRBY [KEY]
     * @param key
     * @return Long
     */
    public Long decrBy(String key, long step) {
        return redisTemplate.boundValueOps(key).decrement(step);
    }
    /**
     * DECR 不存在 DECRBYFLOAT （小数 - 小数） 的操作
     */


    /**
     * 这个connection.scan 没法移动cursor，也只能scan一次,而且count似乎也不管用
     * @param count 返回结果数
     * @param pattern
     * @return Set
     */
    public Set<String> scan(long count, String pattern) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(pattern).count(count).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
    }

    /**
     * 使用 MultiKeyCommands,实现 scan [start] match [pattern] count [count]
     * @param pattern
     * @param count 返回结果数
     * @param start
     * @return keys
     */
    public Set<String> scan(String pattern, int count, long start) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();

            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            MultiKeyCommands multiKeyCommands = (MultiKeyCommands) commands;

            ScanParams scanParams = new ScanParams();
            scanParams.match(pattern);
            scanParams.count(count);
            ScanResult<String> scan = multiKeyCommands.scan(String.valueOf(start), scanParams);
            while (Objects.nonNull(scan.getCursor())) {
                keys.addAll(scan.getResult());
                //当 scan.getStringCursor() 存在 且不是 0 的时候，一直移动游标获取
                if (Objects.equals("0", scan.getCursor())) {
                    scan = multiKeyCommands.scan(scan.getCursor(), scanParams);
                }else {
                    break;
                }
            }
            return keys;
        });
    }

    /**
     * String 操作
     */

    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setObj(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * set new value and return its old value
     * @param key
     * @param value
     * @return return its old value, 重写entity的toString会抛出异常，建议不要重写entity的toString方法
     */
    public Object setAndGetOldValue(String key, Object value) {
        return redisTemplate.boundValueOps(key).getAndSet(value);
    }

    public Object get(String key) {
       return redisTemplate.opsForValue().get(key);
    }

    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 获取指定区间范围内的值
     * getrange [key] [start] [end]
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String getrange(String key , long start, long end) {
        return redisTemplate.boundValueOps(key).get(start, end);
    }

    /**
     * 设置指定区间范围内的值
     * setrange [key] [start] [end]
     * @param key
     * @param start
     * @param end
     */
    public void setrange(String key , long start, long end) {
        redisTemplate.boundValueOps(key).set(start, end);
    }

    /**
     * Set 集合操作
     */

    public boolean addSet(String key, Object... values) {
        Long add = redisTemplate.opsForSet().add(key, values);
        return Objects.nonNull(add) && add > 0;
    }

    public Set<Object> getMembers(String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    public boolean containSet(String key, Object value) {
        Boolean member = redisTemplate.boundSetOps(key).isMember(value);
        return Boolean.TRUE.equals(member);
    }

    public Long sizeSet(String key) {
        return redisTemplate.boundSetOps(key).size();
    }

    public boolean removeValue(String key, Object... values) {
        Long remove = redisTemplate.boundSetOps(key).remove(values);
        return Objects.nonNull(remove) && remove > 0;
    }

    public Object randomMembers(String key) {
        return redisTemplate.boundSetOps(key).randomMember();
    }

    public Object randomPop(String key) {
        return redisTemplate.boundSetOps(key).pop();
    }

    /**
     * 将 sourceKey 中执定的 值value 移除  加入到 destKey 集合中
     * @param sourceKey
     * @param destKey
     * @param value
     * @return boolean
     */
    public boolean moveValue(String sourceKey, String destKey, Object value) {
        Boolean move = redisTemplate.boundSetOps(sourceKey).move(destKey, value);
        return Boolean.TRUE.equals(move);
    }

    /**
     * 差集(在第一个set里面而不在后面任何一个set里面的项)
     * @param key1
     * @param key2
     * @return 没有差集，返回 []
     */
    public Set<Object> diffSet(String key1, String key2) {
        return redisTemplate.boundSetOps(key1).diff(key2);
    }

    /**
     * 交集(在第一个set和第二个set中都有的)
     * @param key1
     * @param key2
     * @return 没有交集，返回 []
     */
    public Set<Object> intersectSet(String key1, String key2) {
        return redisTemplate.boundSetOps(key1).intersect(key2);
    }

    /**
     * 并集(两个集合所有元素)
     * @param key1
     * @param key2
     * @return 没有并集，返回 []
     */
    public Set<Object> unionSet(String key1, String key2) {
        return redisTemplate.boundSetOps(key1).union(key2);
    }

    /**
     * List 集合操作
     */

    public boolean leftPush(String key, Object... values) {
        Long pushAll = redisTemplate.boundListOps(key).leftPushAll(values);
        return Objects.nonNull(pushAll) && pushAll > 0;
    }

    public boolean rightPush(String key, Object... values) {
        Long pushAll = redisTemplate.boundListOps(key).rightPushAll(values);
        return Objects.nonNull(pushAll) && pushAll > 0;
    }

    /**
     * LSET: 设置 index 位置的list元素的值为 value
     * @param key
     * @param index
     * @param value
     * @return boolean
     * @throws redis.clients.jedis.exceptions.JedisDataException 超出size
     */
    public boolean setList(String key, long index, Object value) {
        redisTemplate.boundListOps(key).set(index, value);
        return true;
    }

    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
     *
     * - count > 0: 从头往尾移除值为 value 的元素。
     * - count < 0: 从尾往头移除值为 value 的元素。
     * - count = 0: 移除所有值为 value 的元素。
     * 比如， LREM list -2 “hello” 会从存于 list 的列表里移除最后两个出现的 “hello”。
     *
     * @param key
     * @param count
     * @param value
     * @return 删除个数, 如果list里没有存在key就会被当作空list处理，所以当 key 不存在的时候，这个命令会返回 0
     */
    public Long removeList(String key, long count, Object value) {
        return redisTemplate.boundListOps(key).remove(count, value);
    }

    public Object leftPop(String key) {
        return redisTemplate.boundListOps(key).leftPop();
    }

    public Object rightPop(String key) {
        return redisTemplate.boundListOps(key).rightPop();
    }

    public Object getByIndex(String key, long index) {
        return redisTemplate.boundListOps(key).index(index);
    }

    public List<Object> getByRange(String key, long start, long end) {
        return redisTemplate.boundListOps(key).range(start, end);
    }

    public long listSize(String key) {
        Long size = redisTemplate.boundListOps(key).size();
        return Objects.isNull(size) ? 0 : size;
    }

    /**
     * RPOPLPUSH:
     * 原子性地返回并移除存储在 source 的列表的最后一个元素（列表尾部元素），
     * 并把该元素放入存储在 destination 的列表的第一个元素位置（列表头部）。
     * @param sourceList
     * @param destList
     * @return
     */
    public Object rightPopLeftPush(String sourceList, String destList) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceList, destList);
    }

    /**
     * LPUSHX:
     * 只有当 key 已经存在并且存着一个 list 的时候，在这个 key 下面的 list 的头部插入 value。 与 LPUSH 相反，当 key 不存在的时候不会进行任何操作。
     * @param key
     * @param value
     * @return list size
     */
    public Long leftExistPush(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * RPUSHX:
     * 只有当 key 已经存在并且存着一个 list 的时候，在这个 key 下面的 list 的尾部插入 value。 与 LPUSH 相反，当 key 不存在的时候不会进行任何操作。
     * @param key
     * @param value
     * @return list size
     */
    public Long rightExistPush(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }


    /**
     * Hash操作
     */

    /**
     * HSET [KEY] [MEMBER KEY] [MEMBER VALUE]
     * @param key
     * @param values Map
     * @return boolean
     */
    public boolean hashSet(String key, Map<?, Object> values) {
        redisTemplate.boundHashOps(key).putAll(values);
        return true;
    }

    /**
     * HSETNX [KEY] [MEMBER KEY] [VALUE]
     * 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。如果字段已存在，该操作无效果。
     * @param key
     * @param memberKey
     * @param value
     * @return boolean
     */
    public boolean hashSetNx(String key, Object memberKey, Object value) {
        Boolean putIfAbsent = redisTemplate.boundHashOps(key).putIfAbsent(memberKey, value);
        return Boolean.TRUE.equals(putIfAbsent);
    }

    /**
     * HGETALL: 返回 key 指定的哈希集中所有的字段和值。返回值中，每个字段名的下一个是它的值，所以返回值的长度是哈希集大小的两倍
     * @param key
     * @return Map
     */
    public Map<Object, Object> hashGet(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    /**
     * HGET [KEY] [MEMBER KEY]
     * @param key
     * @param memberKey
     * @return MEMBER VALUE
     */
    public Object hashGet(String key, Object memberKey) {
        return redisTemplate.boundHashOps(key).get(memberKey);
    }

    /**
     * HEXIST [KEY] [MEMBER KEY]: 返回hash里面memberKey是否存在
     * @param key
     * @param memberKey
     * @return boolean
     */
    public boolean hashIfExist(String key, Object memberKey) {
        Boolean hasKey = redisTemplate.boundHashOps(key).hasKey(memberKey);
        return Boolean.TRUE.equals(hasKey);
    }

    /**
     * HINCRBY [KEY] [MEMBER KEY] [NUMBER]
     * 增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0
     * HINCRBY 支持的值的范围限定在 64位 有符号整数
     * @param key
     * @param memberKey
     * @param num 加数
     * @return Long
     * @throws redis.clients.jedis.exceptions.JedisDataException  ERR hash value is not a long;
     */
    public Long hashIncrBy(String key, Object memberKey, long num) {
        return redisTemplate.boundHashOps(key).increment(memberKey, num);
    }

    /**
     * HINCRBYFLOAT [KEY] [MEMBER KEY] [NUMBER]
     * 为指定key的hash的field字段值执行float类型的increment加。如果field不存在，则在执行该操作前设置为0.
     * 如果出现下列情况之一，则返回错误：
     *      field的值包含的类型错误(不是字符串)。
     *      当前field或者increment不能解析为一个float类型。
     *
     * @param key
     * @param memberKey
     * @param num
     * @return Double
     * @throws redis.clients.jedis.exceptions.JedisDataException  ERR hash value is not a float;
     */
    public Double hashIncrByFloat(String key, Object memberKey, double num) {
        return redisTemplate.boundHashOps(key).increment(memberKey, num);
    }

    /**
     * HKEYS [KEY]
     * 返回 key 指定的哈希集中所有字段的名字。
     * @param key
     * @return Set
     */
    public Set<Object> hashGetKeys(String key) {
        return redisTemplate.boundHashOps(key).keys();
    }

    /**
     * HVALS [KEY]
     * 返回 key 指定的哈希集中所有字段的值
     * @param key
     * @return list
     */
    public List<Object> hashGetAllValues(String key) {
        return redisTemplate.boundHashOps(key).values();
    }

    /**
     * HVALS [KEY]
     * 返回 key 指定的哈希集中所有字段的值, 指定返回的值
     * @param key
     * @param memberKeys
     * @return
     */
    public List<Object> hashGetAllValues(String key, Object... memberKeys) {
        List<Object> list = Arrays.asList(memberKeys);
        return redisTemplate.boundHashOps(key).multiGet(list);
    }

    /**
     * HLEN [KEY]
     * 返回 key 指定的哈希集包含的字段的数量
     * @param key
     * @return Long
     */
    public Long hashSize(String key) {
        return redisTemplate.boundHashOps(key).size();
    }

    /**
     * HDEL [KEY] [MEMBER KEY]......
     * @param key
     * @param memberKeys
     * @return boolean
     */
    public boolean hashDel(String key, Object... memberKeys) {
        Long delete = redisTemplate.boundHashOps(key).delete(memberKeys);
        return Objects.nonNull(delete) && delete > 0;
    }

    /**
     * HSTRLEN [KEY] [MEMBER KEY]
     * 返回hash指定field的value的字符串长度，如果hash或者field不存在，返回0.
     * @param key
     * @param memberKey
     * @return long
     */
    public long hashValueStrLen(String key, Object memberKey) {
        Long lengthOfValue = redisTemplate.boundHashOps(key).lengthOfValue(memberKey);
        return Objects.nonNull(lengthOfValue) ? lengthOfValue : 0;
    }

    /**
     * HSCAN [KEY] [START] MATCH [PATTERN] COUNT [COUNT]
     * 不阻塞的扫描 hash 值
     * 注意： 键 数据量太小的话 START 和 COUNT 不生效(测试数据量 2000管用， 200不管用); 键值数据量小，用 HGETALL 效率更高
     * @param key
     * @param start
     * @param pattern
     * @param count
     * @return List<Map.Entry<String, String>>
     */
    public List<Map.Entry<String, String>> hashScan(String key, long start, String pattern, int count) {

        return redisTemplate.execute((RedisCallback<List<Map.Entry<String, String>>>) connection -> {
            JedisCommands jedisCommands = (JedisCommands) connection.getNativeConnection();

            ScanParams scanParams = new ScanParams();
            scanParams.match(pattern);
            scanParams.count(count);
            ScanResult<Map.Entry<String, String>> scanResult = jedisCommands.hscan(key, String.valueOf(start), scanParams);
            if(Objects.isNull(scanResult)) {
                return null;
            }
            return scanResult.getResult();
        });
    }

    /**
     * HSCAN [KEY] [START] MATCH [PATTERN] COUNT [COUNT]
     * 不阻塞的扫描 hash 值
     * 注意： 键 数据量太小的话  不生效 (测试数据量 2000管用， 200不管用); 键值数据量小，用 HGETALL 效率更高
     * @param key
     * @param start
     * @param pattern
     * @param count
     * @return List<Map.Entry<String, String>>
     */
    public Map<Object, Object> hScan(String key, long start, String pattern, int count) {
        Map<Object, Object> map = new HashMap<>();
        ScanOptions scanOptions = new ScanOptions.ScanOptionsBuilder().match(pattern).count(count).build();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key,scanOptions);
        try {
            while (cursor.hasNext()) {
                if(!cursor.isClosed() && cursor.getPosition() < start) {
                    cursor.next();
                    continue;
                }
                Map.Entry<Object, Object> next = cursor.next();
                map.put(next.getKey(), next.getValue());
                if(map.size() == count) {
                    break;
                }
            }
            if(!cursor.isClosed()){
                cursor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map.size() == 0 ? null : map;
    }

}
