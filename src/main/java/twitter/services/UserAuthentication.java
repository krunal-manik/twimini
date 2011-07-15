package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/5/11
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserAuthentication {
    private static SimpleJdbcTemplate db;

    @Autowired
    public UserAuthentication(SimpleJdbcTemplate db){
        this.db = db;
    }

    public static User authenticateUser( String username , String password ){
        User data = null;
        try{
            data = db.queryForObject( "SELECT user_id, username , password from user where username = ?",
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet rs, int i) throws SQLException {
                            User ret = new User();
                            ret.setUserId( rs.getInt("user_id"));
                            ret.setUsername(rs.getString("username"));
                            ret.setPassword(rs.getString("password"));
                            return ret;
                        }
                    }, username );
        }
        catch( EmptyResultDataAccessException ex ){
        }
        catch( Exception ex ){
            System.out.println( "Bug in user authentication :((" );
            ex.printStackTrace();
        }
        if( data == null )return data;
        return data.getPassword().equals(password) ? data : null;
    }

    public static void registerUser( String username , String password , String name , String email ){
        try{
            db.update("INSERT INTO User(username,password,name,email) VALUES ( ? , ? , ? , ? )", username, password, name, email);
        }
        catch( Exception ex ){
            System.out.println( "Register User Exception :((((((" );
            ex.printStackTrace();
        }
    }

    public static User getUserByUsername( String username ) {
        User data = null;
        try{
            data = db.queryForObject( "SELECT user_id, username , password from user where username = ?",
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet rs, int i) throws SQLException {
                            User ret = new User();
                            ret.setUserId( rs.getInt("user_id"));
                            ret.setUsername(rs.getString("username"));
                            ret.setPassword(rs.getString("password"));
                            return ret;
                        }
                    }, username );
        }
        catch( EmptyResultDataAccessException ex ){
        }
        catch( Exception ex ) {
            System.out.println( "Bug in user exists :(( " );
            ex.printStackTrace();
        }
        return data;
    }

    public static User getPassword( String email ){
        User data = null;
        try{
            data = db.queryForObject( "SELECT user_id, username , password from user where email = ?",
                    new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet rs, int i) throws SQLException {
                            User ret = new User();
                            ret.setUserId( rs.getInt("user_id"));
                            ret.setUsername(rs.getString("username"));
                            ret.setPassword(rs.getString("password"));
                            return ret;
                        }
                    }, email );
        }
        catch( EmptyResultDataAccessException ex ){
        }
        catch( Exception ex ) {
            System.out.println( "Bug in user exists :(( " );
            ex.printStackTrace();
        }
        return data;
    }

    public static void insertToken( User user , String token ) {

        try{
            db.update( "INSERT into tokens values( ? , ? , NOW() )" , user.getUsername() , token );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in insertToken :(( " );
            ex.printStackTrace();
        }
    }

    public static boolean tokenExists( String username , String token ) {
        int count = 0;
        try{
            count = db.queryForInt( "SELECT count(*) from tokens where username = ? and token = ? " ,
                    username , token );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in tokenExists :(( " );
            ex.printStackTrace();
        }
        return count == 1;
    }

    public static void deleteToken( String username , String token ) {
        try{
            db.update( "delete from tokens where username = ? and token = ? " ,
                    username , token );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in deleteToken :(( " );
            ex.printStackTrace();
        }
    }

    public static void updatePassword( String username , String password ) {
        try{
            db.update( "update user set password = ? where username = ?" ,
                    password , username );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in updatePassword :(( " );
            ex.printStackTrace();
        }
    }
}
