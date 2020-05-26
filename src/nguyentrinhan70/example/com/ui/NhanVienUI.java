package nguyentrinhan70.example.com.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class NhanVienUI extends JFrame {
	JComboBox<String> cboPhongBan;
	Connection conn;
	PreparedStatement preparedStatement;
	
	JList<String> listNhanVien;
	
	JTextField txtMa, txtTen;
	JButton btnLuuNhanVien, btnXoaNhanVien, btnXoaPhongBan;
	
	String mapb="";//Lưu mã phòng ban đang chọn
	String manv="";//lưu mã nhân viên đang chọn
	public NhanVienUI(String title){
		super(title);
		addControls();
		addEvents();
		ketNoiDuLieu();
		hienThiDanhSachPhongBan();
	}

	private void hienThiDanhSachPhongBan() {
		// TODO Auto-generated method stub
		try{
			
			preparedStatement = conn.prepareStatement("Select * from PhongBan" );
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String pb = resultSet.getString("maPhongBan") + "-" + resultSet.getString("TenPhongBan");
				cboPhongBan.addItem(pb);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	private void ketNoiDuLieu() {
		// TODO Auto-generated method stub
		try{
			
			String dataBase = "csdl/dbSinhVien.accdb";
			String strConn = "jdbc:ucanaccess://" + dataBase;
			conn = DriverManager.getConnection(strConn);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void addEvents() {
		// TODO Auto-generated method stub
		cboPhongBan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cboPhongBan.getSelectedIndex()==-1) return;
				mapb =  cboPhongBan.getSelectedItem().toString().split("-")[0];
				xemDanhSachNhanVienTheoPhongBan(mapb);
			}
		});
		
		btnLuuNhanVien.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyLuuNhanVien();
			}
		});
		listNhanVien.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(listNhanVien.getSelectedIndex()==-1) return;
				manv = listNhanVien.getSelectedValue().split("-")[0];
				txtMa.setText(manv);
				txtTen.setText(listNhanVien.getSelectedValue().split("-")[1]);
			}
		});
		btnXoaNhanVien.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				xuLyXoaNhanVien();
				
			}
		});
		btnXoaPhongBan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				xuLyXoaPhongBan();
			}
		});
	}
	public int demSoNhanVienTrongPhongBan(String mapb){
		try{
			String sql = "select count(*) from SinhVien where maphongban = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, mapb);
			ResultSet  resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return resultSet.getInt(1);
			}
			return 0;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0; 
	}
	protected void xuLyXoaPhongBan() {
		// TODO Auto-generated method stub
		try{
			int sonv = demSoNhanVienTrongPhongBan(mapb);
			if(sonv>0){
				int ret = JOptionPane.showConfirmDialog(null
						, "Phòng ban này có" + sonv + "Bạn có chắc chắn xóa không?",
						"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
				if(ret == JOptionPane.NO_OPTION){
					return;
				}
					String sql = "delete from PhongBan where maPhongBan = ?";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.setString(1, mapb);
					int x = preparedStatement.executeUpdate();
					if(x>0){
						hienThiDanhSachPhongBan();
					}
				}
			
			JOptionPane.showConfirmDialog(null,"có " + sonv);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	protected void xuLyXoaNhanVien() {
		// TODO Auto-generated method stub
		try{
			String sql ="delete from SinhVien where ma =?";
			preparedStatement= conn.prepareStatement(sql);
			preparedStatement.setString(1, manv);
			int x = preparedStatement.executeUpdate();
			if(x>0){
				xemDanhSachNhanVienTheoPhongBan(mapb);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public boolean kiemTraMaNVTonTai(String maNV) {
		try{
			String sql = "Select * from SinhVien where ma  = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, maNV);
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	protected void xuLyLuuNhanVien() {
		// TODO Auto-generated method stub
		if(kiemTraMaNVTonTai(txtMa.getText())==false){

			try
			{
				String sql= "insert into SinhVien(Ma,Ten,MaPhongBan) values(?,?,?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, txtMa.getText());
				preparedStatement.setString(2, txtTen.getText());
				preparedStatement.setString(3, mapb);
				int x = preparedStatement.executeUpdate();
				if(x>0){
					xemDanhSachNhanVienTheoPhongBan(mapb);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		else
		{
			try{
				String sql = "Update SinhVien set ten=? where ma=?";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, txtTen.getText());
				preparedStatement.setString(2, txtMa.getText());
				int x = preparedStatement.executeUpdate();
				if(x>0){
					xemDanhSachNhanVienTheoPhongBan(mapb);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	protected void xemDanhSachNhanVienTheoPhongBan(String mapb) {
		// TODO Auto-generated method stub
		try{
			String sql = "Select * from SinhVien where maPhongban = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, mapb);//Gán giá trị cho ?
			ResultSet resultSet = preparedStatement.executeQuery();
			Vector<String> vec = new Vector<>();
			while(resultSet.next()){
				vec.add(resultSet.getString("ma") + "-" + resultSet.getString("ten"));
				
			}
			listNhanVien.setListData(vec);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	private void addControls() {
		// TODO Auto-generated method stub
		Container con = getContentPane();
		con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));
		JPanel pnPhongBan = new JPanel();
		pnPhongBan.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPhongBan = new JLabel("Chọn phòng ban");
		cboPhongBan = new JComboBox<>();
		cboPhongBan.setPreferredSize(new Dimension(300, 30));
		pnPhongBan.add(lblPhongBan);
		pnPhongBan.add(cboPhongBan);
		btnXoaPhongBan = new JButton("Xóa phòng ban");
		pnPhongBan.add(btnXoaPhongBan);
		con.add(pnPhongBan);
				
		JPanel pnNhanVien  = new JPanel();
		pnNhanVien.setLayout(new BorderLayout());
		listNhanVien = new JList<>();
		JScrollPane sc  = new JScrollPane(listNhanVien,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnNhanVien.add(sc,BorderLayout.CENTER);
		con.add(pnNhanVien);
				
		JPanel pnChiTiet = new JPanel();
		pnChiTiet.setLayout(new BoxLayout(pnChiTiet, BoxLayout.Y_AXIS));
		con.add(pnChiTiet, BorderLayout.SOUTH);
		
		JPanel pnMa = new JPanel();
		pnMa.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMa = new JLabel("Mã:");
		txtMa = new JTextField(15);
		pnMa.add(lblMa);
		pnMa.add(txtMa);
		
		JPanel pnTen = new JPanel();
		pnTen.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTen = new JLabel("Tên:");
		txtTen = new JTextField(15);
		pnMa.add(lblTen);
		pnMa.add(txtTen);
		
		pnChiTiet.add(pnMa);
		pnChiTiet.add(pnTen);
		
		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnLuuNhanVien = new JButton("Lưu sinh viên");
		pnButton.add(btnLuuNhanVien);
		pnChiTiet.add(pnButton);
		
		btnXoaNhanVien = new JButton("Xóa nhân viên");
		pnButton.add(btnXoaNhanVien);
		
	}
	public void showWindow(){
		this.setSize(700, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

