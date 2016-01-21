/**
 * Returns custom landing path provided in Role's custom attribute for a Role current user belongs
 * to
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class RolePageType implements LandingPageType
{

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath(javax.servlet
	 * .http.HttpServletRequest)
	 */
	public String getLandingPagePath(final HttpServletRequest request) throws PortalException,
			SystemException
	{
		String rolePath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);

		Role role = getUserRole(currentUser, CustomLandingPageUtil.getIncludeSystemRole());
		if (Validator.isNotNull(role))
		{
			rolePath = CustomLandingPageUtil.getLandingPageFriendlyURL(role,
					PortalUtil.getCompanyId(request));
		} else
		{
			LOG.debug("No Role assigned to user :  " + currentUser.getFullName());
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
			List<Role> roles = user.getRoles();
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
					for (Role role : roles)
					{
						if (!PortalUtil.isSystemRole(role.getName()))
						{
							userRole = role;
							break;
						}
					}
				}
			}
		} catch (SystemException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return userRole;
	}

	private static final Log LOG = LogFactoryUtil.getLog(RolePageType.class);
}
