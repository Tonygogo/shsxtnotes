package com.shsxt.service;

import java.util.List;

import com.shsxt.dao.TypeDao;
import com.shsxt.po.NoteType;
import com.shsxt.util.StringUtil;
import com.shsxt.vo.ResultInfo;

public class TypeService {
	
	private TypeDao typeDao = new TypeDao();

	/**
	 * 通过用户ID查询类型集合
	 * @param userId
	 * @return
	 */
	public ResultInfo<List<NoteType>> findNoteTypeList(Integer userId) {
		ResultInfo<List<NoteType>> resultInfo = new ResultInfo<>();
		// 调用Dao层的查询方法
		List<NoteType> list = typeDao.findNoteTypeList(userId);
		if (list == null || list.size() < 1) { // 说明当前用户没有类型集合
			resultInfo.setCode(0);
			resultInfo.setMsg("暂未查询到云记类型！");
		} else {
			resultInfo.setCode(1);
			resultInfo.setResult(list);
		}
		return resultInfo;
	}

	/**
	 * 验证当前用户下类型名的唯一性
	 * @param userId
	 * @param typeId
	 * @param typeName
	 * @return
	 */
	public ResultInfo<NoteType> checkTypeName(Integer userId, String typeId, String typeName) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (StringUtil.isNullOrEmpty(typeName)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名不能为空！");
			return resultInfo;
		}
		
		// 调用Dao层的方法，查询类型名是否存在
		boolean flag = typeDao.checkTypeName(userId,typeId,typeName);
		if (flag) { // 可用
			resultInfo.setCode(1);
			resultInfo.setMsg("类型名可使用！");
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名被占用，不可使用！");
		}
		
		return resultInfo;
	}

	/**
	 * 添加或者修改操作
	 * 		1、判断参数是否为空（类型名称）
			2、调用Dao层的更新方法，返回受影响的行数						
			3、return resultInfo
	 * @param userId
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public ResultInfo<NoteType> addOrUpdate(Integer userId, String typeName, String typeId) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (StringUtil.isNullOrEmpty(typeName)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名不能为空！");
			return resultInfo;
		}
		// 调用Dao层的更新方法，返回受影响的行数		
		int row  = typeDao.addOrUpdate(userId, typeName, typeId);
		if (row > 0) { // 成功
			resultInfo.setCode(1);
			resultInfo.setMsg("更新成功！");
			// 通过userId和typeName查询类型对象
			NoteType noteType = typeDao.findNoteType(userId,typeName);
			resultInfo.setResult(noteType);
		} else { // 失败
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败！");
		}
		return resultInfo;
	}

	/**
	 * 删除类型
	 * 	删除前需要查询是否有子记录
	 * @param typeId
	 * @return
	 */
	public ResultInfo<NoteType> deleteType(String typeId) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (StringUtil.isNullOrEmpty(typeId)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("系统异常！");
			return resultInfo;
		}
		// 通过typeId查询是否有子记录(通过类型ID查询对应类型的云记数量)
		Long count = typeDao.countNoteNums(typeId);
		// 如果有子记录，提示删除失败，返回
		if (count > 0) {
			resultInfo.setCode(0);
			resultInfo.setMsg("存在子记录，不能删除！");
			return resultInfo;
		}
		
		// 如果没有子记录，执行删除，返回受影响的行数
		int row = typeDao.deleteType(typeId);
		
		// 判断是否删除成功
		if (row > 0) {
			resultInfo.setCode(1);
			resultInfo.setMsg("删除成功！");
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("删除失败！");
		}
		
		return resultInfo;
	}

}
