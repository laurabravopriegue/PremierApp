<?php
    $con = mysqli_connect("localhost", "id451584_quizapp", "submeseta", "id451584_premier");
    mysqli_set_charset($con, 'utf8');
    $name = $_POST["name"];
    $age = $_POST["age"];
    $username = $_POST["username"];
    $password = $_POST["password"];

    $response = array();
    $response["success"] = false;  

    $check = "SELECT * FROM user WHERE username = '$username'";
    $rs = mysqli_query($con,$check);
    $data = mysqli_fetch_array($rs, MYSQLI_NUM);
    if($data[0] > 1) 
    {
        //user exists already
    }
    else
    {
        $statement = mysqli_prepare($con, "INSERT INTO user (name, username, age, password) VALUES (?, ?, ?, ?)");
        mysqli_stmt_bind_param($statement, "ssis", $name, $username, $age, $password);
        mysqli_stmt_execute($statement);
        $response["success"] = true;  
    }
    echo json_encode($response);
?>