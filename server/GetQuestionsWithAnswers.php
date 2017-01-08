<?php
    $con = mysqli_connect("localhost", "id451584_quizapp", "submeseta", "id451584_premier");
        mysqli_set_charset($con, 'utf8');
    $userId = $_POST["userId"];
    $check = "SELECT * from question LEFT JOIN (SELECT * FROM answer WHERE answer.user_id = ".$userId.") AS userAnswers on question.id= userAnswers.id;";
    $rs = mysqli_query($con,$check);
    $response = array();
    while ($data = mysqli_fetch_array($rs, MYSQLI_ASSOC)) 
    {
        $response[] = $data;
    }
    echo json_encode($response);