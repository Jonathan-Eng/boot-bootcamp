package mybatis.mappers;
import org.apache.ibatis.annotations.Param;
import pojos.Account;

public interface AccountMapper {

    int insert(Account ac);

    Account getAccountByName(String name);
    Account getAccountByToken(@Param("token") String token);
    Account getAccountByEsIndex(@Param("esIndex") String esIndex);
}
