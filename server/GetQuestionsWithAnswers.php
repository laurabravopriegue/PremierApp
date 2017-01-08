<?php
    $userId = $_GET["userId"];
    $con = mysqli_connect("localhost", "id451584_quizapp", "submeseta", "id451584_premier");
    mysqli_set_charset($con, 'utf8');
    $check = "SELECT question.question, question.id, question.answer, question.explanation, userAnswers.user_id, userAnswers.answer AS userAnswer from question LEFT JOIN (SELECT * FROM answer WHERE answer.user_id = ".$userId.") AS userAnswers on question.id= userAnswers.id;";
    $rs = mysqli_query($con,$check);
    $response = array();
    while ($data = mysqli_fetch_array($rs, MYSQLI_ASSOC)) 
    {
        $response[] = $data;
    }
    echo json_encode($response);