<?php
include "connect.php";
$email=$_POST['email'];

//checkdata
$query = 'SELECT * FROM `tbl_account` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$numrow = mysqli_num_rows($data);
if ($numrow > 0){
    //nếu có tài khoản thì đăng nhập luôn
    $result = array();
    while ($row = mysqli_fetch_assoc($data)) {
        $result[] = ($row);
    }
	if (!empty($result)){
        $arr = [
            'success' => true,
            'message' => "Lay thong tin dang nhap thanh cong",
            'result' => $result
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Lay thong tin dang nhap that bai",
            'result' => $result
        ];
    }
} else {
	//nếu chưa thì đăng ký sau đó đăng nhập
	$query = 'INSERT INTO `tbl_account`(`email`, `role`, `status`) VALUES ("'.$email.'", "1", "1")';
	$data = mysqli_query($conn, $query);
    
	if ($data == true){ // dang ky thanh cong
        $query = 'SELECT * FROM `tbl_account` WHERE `email` = "'.$email.'"';
        $data = mysqli_query($conn, $query);
        $result = array();
        while ($row = mysqli_fetch_assoc($data)) {
            $result[] = ($row);
        }
        if (!empty($result)){
            $arr = [
                'success' => true,
                'message' => "dang nhap thanh cong",
                'result' => $result
            ];
        } else {
            $arr = [
                'success' => false,
                'message' => "dang nhap that bai",
                'result' => $result
            ];
        }
	} else {
		$arr = [
			'success' => false,
			'message' => " Dang ky that bai"
		];
	}
}

print_r(json_encode($arr));
?>