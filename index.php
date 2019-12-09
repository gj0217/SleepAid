<?php
function My_login($username,$password)
{
    $con_db = new mysqli("localhost", "root", "root", "SleepAid_db");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command = "SELECT user_name FROM tb_Users WHERE user_name='{$username}' and password='{$password}'";
    $result = $con_db->query($sql_command);
    if ($result->num_rows > 0) {
        echo 'Succeed:' . $username;
    } else {
        echo 'Failed';
    }
    $con_db->close();
}

function My_register($username,$password)
{
    $con_db = new mysqli("localhost", "root", "root", "SleepAid_db");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_exists = "SELECT count(user_name) FROM tb_users WHERE user_name = '{$username}'";
    //echo $sql_exists;
    $judge = $con_db->query($sql_exists);
    if($judge->num_rows > 0){
        $row = $judge->fetch_assoc();
        $count = $row['count(user_name)'];
        {
            if($count > 0){
                echo 'User exists';
            }else{
                $sql_command = "INSERT INTO tb_users (user_name, password) VALUES ('{$username}','{$password}')";
                $result = $con_db->query($sql_command);
                if($result){
                    echo 'Succeed';
                }else{
                    echo 'Failed';
                }
            }
        }
        
    }else{
        echo 'Failed';
    }
    $con_db->close();
}
function My_get_info($keywords){
    $con_db = new mysqli("localhost", "root", "root", "SleepAid_db");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $keywords = explode('_',$keywords);
    foreach($keywords as $value){
        $sql_command = "SELECT info FROM tb_info WHERE keywords='{$value}'";
        $result = $con_db->query($sql_command);
        if ($result->num_rows > 0) {
            echo "·".$value." about sleep: \r\n";
            while($row = $result->fetch_assoc()){
                $info = $row['info'];
                echo "\r\n".$info." \n";
            }
        } else {
            echo 'Failed';
        }
    }
    $con_db->close();
}

function My_get_img($music){
    $con_db = new mysqli("localhost", "root", "root", "SleepAid_db");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command = "SELECT url FROM tb_image WHERE music='{$music}'";
    $result = $con_db->query($sql_command);
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $url = $row['url'];
        echo 'Succeed:' . $url;
    } else {
        echo 'Failed';
    }
    $con_db->close();
}


$method=$_POST['method'];
switch($method){
    case 'login':
        $username=$_POST['username'];
        $password=$_POST['password'];
        My_login($username,$password);
        break;
    case 'register':
        $username=$_POST['username'];
        $password=$_POST['password'];
        My_register($username,$password);
        break;
    case 'get_info':
        $keywords=$_POST['keywords'];
        My_get_info($keywords);
        break;
    case 'get_img':
        $music=$_POST['music'];
        My_get_img($music);
        break;
    default:
        break;
}
?>
