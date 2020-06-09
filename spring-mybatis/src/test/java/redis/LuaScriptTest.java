package redis;

import com.learn.redis.LuaScriptService;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LuaScriptTest extends BaseTest {

    @Autowired
    private LuaScriptService luaScriptService;

    @Test
    public void execute() {
        luaScriptService.redisAddScriptExec();
    }
}
