<?php
include "connect.php";
$email=$_POST['email'];
// $pass=$_POST['pass'];
$query = 'SELECT * FROM `tbl_account` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$numrow = mysqli_num_rows($data);
//checkdata
if($numrow > 0){
	$result = array();
    while ($row = mysqli_fetch_assoc($data)) {
        $result[] = ($row);
    }
	if (!empty($result)){
        $status = $result[0]['status'];
        if($status == 1){
            $arr = [
            'success' => true,
            'message' => "Lay thong tin dang nhap thanh cong",
            'result' => $result
            ];
        }else{
            $arr = [
            'success' => false,
            'message' => "Tài khoản của bạn đã bị khóa !!",
            'result' => $result
            ];
        }
    } else {
        $arr = [
            'success' => false,
            'message' => "Lay thong tin dang nhap that bai",
            'result' => $result
        ];
    }
}else{
		$arr = [
            'success' => false,
            'message' => "Sai thông tin đăng nhập !!",
            'result' => $result
        ];
}
print_r(json_encode($arr));
?>