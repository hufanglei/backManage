package models.admin.auth.dao;


import models.admin.auth.AuthorityMenu;

import java.util.Comparator;

/**
 * 自定义的按照AuthorityMenu对象菜单按位置值重新排序
 * @author pwj
 *
 */

public class ComparatorImpl implements Comparator<AuthorityMenu> {
	@Override
	public int compare(AuthorityMenu a1, AuthorityMenu a2) {
		int position1 = a1.getPosition();
		int position2 = a2.getPosition();
		if (position1 > position2) {
			return 1;
		} else if (position1 < position2) {
			return -1;
		} else {
			return 0;
		}
	}
}
