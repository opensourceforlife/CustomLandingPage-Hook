/**
 * Returns custom landing path provided in Role's custom attribute for a Role current user belongs
 * to
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UniqueList;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.UserGroupGroupRole;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class RolePageType implements LandingPageType
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.liferay.opensourceforlife.landingpage.LandingPageType#
	 * getLandingPagePath(javax.servlet .http.HttpServletRequest)
	 */
	public String getLandingPagePath(final HttpServletRequest request)
			throws PortalException, SystemException
	{
		String rolePath = StringPool.BLANK;
		final User currentUser = PortalUtil.getUser(request);
		final List<Role> userRoles = getUserRoles(currentUser,
				CustomLandingPageUtil.getIncludeSystemRole());

		if (Validator.isNull(userRoles) && (LOG.isDebugEnabled()))
		{
			LOG.debug("No Roles assigned to user :  " + currentUser.getFullName());
		}
		if (Validator.isNotNull(userRoles))
		{
			for (final Role role : userRoles)
			{
				final String path = CustomLandingPageUtil.getLandingPageFriendlyURL(role,
						PortalUtil.getCompanyId(request));
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Role: " + role.getName() + " -  RolePath: " + path);
				}
				if (Validator.isNotNull(path))
				{
					rolePath = path;
					break;
				}
			}
		}

		return rolePath;
	}

	/**
	 * @param user
	 * @param includeSystemRole
	 * @return
	 */
	private Role getUserRole(final User user, final boolean includeSystemRole)
	{
		Role userRole = null;
		try
		{
			final List<Role> roles = user.getRoles();
			if (Validator.isNotNull(roles) && !roles.isEmpty())
			{
				// If user is member of more than one Role then it will take
				// first Role from list. If iucludeSystemRole is true then it
				// will also include system roles.
				if (includeSystemRole)
				{
					userRole = roles.get(0);
				} else
				{
					for (final Role role : roles)
					{
						if (!PortalUtil.isSystemRole(role.getName()))
						{
							userRole = role;
							break;
						}
					}
				}
			}
		} catch (final SystemException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return userRole;
	}

	/**
	 * @param user
	 * @param includeSystemRole
	 * @return List<Role> - list of User Roles
	 */
	private List<Role> getUserRoles(final User user, final boolean includeSystemRole)
	{
		final List<Role> userRoles = new UniqueList<Role>();
		try
		{
			final List<Role> roles = new UniqueList<Role>();
			roles.addAll(user.getRoles());
			roles.addAll(getRolesByUserGroup(user));
			roles.addAll(getUserGroupRolesOfUser(user));
			roles.addAll(getUserExplicitRoles(user));

			if (Validator.isNotNull(roles) && !roles.isEmpty())
			{
				for (final Role role : roles)
				{
					if (includeSystemRole
							|| (!includeSystemRole && !PortalUtil.isSystemRole(role.getName())))
					{
						userRoles.add(role);
					}
				}
			}
		} catch (final PortalException e)
		{
			LOG.error(e.getMessage(), e);
		} catch (final SystemException se)
		{
			LOG.error(se.getMessage(), se);
		}

		return userRoles;
	}

	private static List<Role> getRolesByUserGroup(User user) throws SystemException, PortalException
	{
		final List<Role> roles = new UniqueList<Role>();
		final List<UserGroup> userGroups = user.getUserGroups();
		if (Validator.isNotNull(userGroups))
		{
			for (final UserGroup group : userGroups)
			{
				final List<Role> userGroupRoles = RoleLocalServiceUtil
						.getGroupRoles(group.getGroupId());
				if (Validator.isNotNull(userGroupRoles))
				{
					roles.addAll(userGroupRoles);
				}
			}
		}
		return roles;
	}

	private static List<Role> getUserExplicitRoles(User user)
			throws SystemException, PortalException
	{
		final List<Role> roles = new UniqueList<Role>();
		final List<UserGroupRole> userGroupRoles = UserGroupRoleLocalServiceUtil
				.getUserGroupRoles(user.getUserId());
		if (Validator.isNotNull(userGroupRoles))
		{
			for (final UserGroupRole userGroupRole : userGroupRoles)
			{
				roles.add(userGroupRole.getRole());
			}
		}
		return roles;
	}

	private static List<Role> getUserGroupRolesOfUser(User user)
			throws SystemException, PortalException
	{
		final List<Role> roles = new UniqueList<Role>();
		final List<UserGroup> userGroupList = UserGroupLocalServiceUtil
				.getUserUserGroups(user.getUserId());
		final List<UserGroupGroupRole> userGroupGroupRoles = new ArrayList<UserGroupGroupRole>();
		if (Validator.isNotNull(userGroupList))
		{
			for (final UserGroup userGroup : userGroupList)
			{
				userGroupGroupRoles.addAll(UserGroupGroupRoleLocalServiceUtil
						.getUserGroupGroupRoles(userGroup.getUserGroupId()));
			}
		}
		for (final UserGroupGroupRole userGroupGroupRole : userGroupGroupRoles)
		{
			final Role role = RoleLocalServiceUtil.getRole(userGroupGroupRole.getRoleId());
			roles.add(role);
		}
		return roles;
	}

	private static final Log LOG = LogFactory.getLog(RolePageType.class);
}
