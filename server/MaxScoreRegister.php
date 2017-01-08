<?php
    $con = mysqli_connect("localhost", "id451584_quizapp", "submeseta", "id451584_premier");
    mysqli_set_charset($con, 'utf8');
    $userid = $_POST["userid"];
    $score = $_POST["score"];

    $response = array();
    $response["success"] = false;
    
    $statement = mysqli_prepare($con, "UPDATE user SET maxscore =".$score." WHERE user.user_id = ".$userid);
    mysqli_stmt_execute($statement);
    $response["success"] = true;

    echo json_encode($response);
?>