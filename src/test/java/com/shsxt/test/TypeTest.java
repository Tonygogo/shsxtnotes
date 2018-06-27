package com.shsxt.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.shsxt.dao.TypeDao;
import com.shsxt.po.NoteType;

public class TypeTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testFindNoteTypeList() {
		List<NoteType> list = new TypeDao().findNoteTypeList(1);
		for (int i = 0; i < list.size(); i++) {
			NoteType type = list.get(i);
			System.out.println(type.getTypeName());
		}
	}

}
