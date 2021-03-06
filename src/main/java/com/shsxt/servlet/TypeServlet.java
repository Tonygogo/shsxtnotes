package com.shsxt.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shsxt.po.NoteType;
import com.shsxt.po.User;
import com.shsxt.service.TypeService;
import com.shsxt.util.JsonUtil;
import com.shsxt.util.StringUtil;
import com.shsxt.vo.ResultInfo;

/**
 * 类型管理
 */
@WebServlet("/type")
public class TypeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TypeService typeService = new TypeService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到用户行为
		String action = request.getParameter("action");
		if (StringUtil.isNullOrEmpty(action) || "list".equals(action)) {
			// 类型集合
			typeList(request, response);
		} else if ("checkTypeName".equals(action)) {
			// 验证当前用户下类型名的唯一性
			checkTypeName(request, response);
		} else if ("addOrUpdate".equals(action)) {
			// 添加或修改操作
			addOrUpdate(request, response);
		} else if ("deleteType".equals(action)) {
			// 删除类型
			deleteType(request ,response);
		}
	}

	/**
	 * 删除类型
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void deleteType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 接收参数
		String typeId = request.getParameter("typeId");
		// 调用Service的方法，返回resultInfo对象
		ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);
		// 将resultInfo对象转换成JSON字符串，响应给ajax的回调函数
		JsonUtil.toJson(resultInfo, response);
	}


	/**
	 * 添加或修改操作
	 *  Servlet层：
			1、得到参数（类型ID、类型名称）
			2、从session中得到userId
			3、调用Service层，返回resultInfo对象
			4、resultInfo对象转换成Json字符串，响应ajax回调函数
			
		Service层：
			1、判断参数是否为空（类型名称）
			2、调用Dao层的更新方法，返回受影响的行数						
			3、return resultInfo
				
		Dao层：
			通过判断typeId是否为空，不为空执行执行修改，为空执行添加
				判断是添加操作还是修改操作
					如果是添加操作，不需要typeID;
						String sql = " insert into tb_note_type (userid,typename) values (?,?)";
						Object[] params = {userId,typeName};
						BaseDao.executeUpdate(sql,params);
					如果是修改操作，需要typeID
						String sql = "update tb_note_type set typeName = ? where userId = ? and typeID = ?";
						Object[] params = {userId,typeName,typeId};
						BaseDao.executeUpdate(sql,params);
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 接收参数
		String typeId = request.getParameter("typeId");
		String typeName = request.getParameter("typeName");
		// 调用Service层，返回resultInfo对象
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 调用Service层，返回resultInfo对象
		ResultInfo<NoteType> resultInfo = typeService.addOrUpdate(userId, typeName, typeId);
		// resultInfo对象转换成Json字符串，响应ajax回调函数
		JsonUtil.toJson(resultInfo, response);
		
	}


	/**
	 * 验证当前用户下类型名的唯一性
	 * 	Servlet层：
			1、得到参数（类型ID、类型名称）
			2、从session中得到userId
			3、调用Service层，返回resultInfo对象
			4、resultInfo对象转换成Json字符串，响应ajax回调函数
			
		Service层：	
			1、判断参数是否为空（类型名称）
			2、调用Dao层的查询方法，返回resultInfo对象
			3、返回
			
		Dao层：
			判断验证的是修改操作，传三个参数（typeName，userID,typeid）
			判断验证的是添加操作，传两个参数（typeName，userID）
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void checkTypeName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 接收参数
		String typeId = request.getParameter("typeId");
		String typeName = request.getParameter("typeName");
		// 调用Service层，返回resultInfo对象
		User user = (User) request.getSession().getAttribute("user");
		ResultInfo<NoteType> resultInfo = typeService.checkTypeName(user.getUserId(),typeId,typeName);
		// resultInfo对象转换成Json字符串，响应ajax回调函数
		JsonUtil.toJson(resultInfo, response);
	}

	/**
	 * 查询类型集合
	 *	类型管理
			Servlet层：
				1、从session中的到用户对象，得到userId
				2、调用Service层的查询，通过useerId查询当前用户的类型集合，返回resultInfo封装对象
				3、将resultInfo对象存到request作用域中
				4、设置动态页面值，请求转发到首页
			
			Service层：
				1、调用Dao层的查询方法，返回类型集合
				2、return
				
			Dao层：
				通过用户ID查询类型集合
				String sql = "select * from tb_note_type where userid = ?";
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 从session中的到用户对象，得到userId
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 调用Service层的查询，通过useerId查询当前用户的类型集合，返回resultInfo封装对象
		ResultInfo<List<NoteType>> resultInfo = typeService.findNoteTypeList(userId);
		// 将resultInfo对象存到request作用域中
		request.setAttribute("resultInfo", resultInfo);
		// 设置动态页面值
		request.setAttribute("changePage", "noteType/list.jsp");
		// 请求转发到首页
		request.getRequestDispatcher("main.jsp").forward(request, response);
		
	}

}
