import org.junit.Test;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class TestBCrypt {
    @Test
    public void testEncode(){
        String gensalt = BCrypt.gensalt();//这个是盐  29个字符，随机生成
        System.out.println(gensalt);
        String password = BCrypt.hashpw("123456", gensalt);  //根据盐对密码进行加密
        System.out.println(password);//加密后的字符串前29位就是盐
    }

    @Test
    public void testDecode(){
        boolean checkpw = BCrypt.checkpw("123456",     "$2a$10$61ogZY7EXsMDWeVGQpDq3OBF1.phaUu7.xrwLyWFTOu8woE08zMIW");
        System.out.println(checkpw);
    }
}
