package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GroupByTest
 *
 * @author darui.wu
 * @create 2020/6/20 9:33 下午
 */
public class GroupByTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_groupBy() throws Exception {
        UserQuery query = new UserQuery()
            .selectId()
            .where.id().eq(24L).end()
            .groupBy.apply("user_name", "age").end()
            .last("/** comment **/");
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE id = ? GROUP BY user_name, age /** comment **/");
    }

    @Test
    public void test_groupBy_having() throws Exception {
        UserQuery query = new UserQuery()
            .select("count(1)", "sum(1)")
            .where
            .id().eq(24L).end()
            .groupBy
            .userName().age().end()
            .having
            .apply("count(1)").gt(10)
            .apply("sum(age)").gt(3)
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT count(1), sum(1) FROM t_user " +
                "WHERE id = ? GROUP BY user_name, age " +
                "HAVING count(1) > ? AND sum(age) > ?");
    }

    @DisplayName("按级别grade统计年龄在15和25之间的人数在10人以上，该条件内最大、最小和平均年龄")
    @Test
    public void test_count_gt_10_groupByGrade() throws Exception {
        UserQuery query = new UserQuery()
            .select.grade().as().id().count().age().max().age().min().age().avg().end()
            .where
            .age().between(15, 25).end()
            .groupBy
            .grade().end()
            .having
            .id().count().gt(10)
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT grade, COUNT(id), MAX(age), MIN(age), AVG(age) " +
                "FROM t_user " +
                "WHERE age BETWEEN ? AND ? " +
                "GROUP BY grade " +
                "HAVING COUNT(id) > ?");
    }
}