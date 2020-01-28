package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import mybatis.mappers.AccountMapper;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.mybatis.guice.MyBatisModule;

import java.util.Properties;

public class AccountsDbModule extends MyBatisModule {
    private static String CONFIG_FILE_NAME = "mybatis/mybatis.config";

    @Override
    protected void initialize() {
        install(JdbcHelper.MySQL);  // helps in finding mysql drive
        bindDataSourceProviderType(PooledDataSourceProvider.class); // the DataSource provider is a Pooled one
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        Names.bindProperties(this.binder(), getMyBatisProperties());
        addMapperClass(AccountMapper.class);    // add AccountMapper to mybatis mappers

        // explicit binding
        bind(DefaultObjectWrapperFactory.class);
        bind(DefaultObjectFactory.class);
    }

    private Properties getMyBatisProperties() {
        return ConfigurationFactory.create(ConfigFileFinder.findRealPath(CONFIG_FILE_NAME), Properties.class);
    }
}
