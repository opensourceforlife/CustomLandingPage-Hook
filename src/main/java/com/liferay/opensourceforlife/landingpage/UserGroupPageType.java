/**
 * Returns custom landing path provided in User Group's custom attribute for a User Group current
 * user belongs to
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class UserGroupPageType implements LandingPageType
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
		UserGroup userGroup = getUserUserGroup(currentUser,
				CustomLandingPageUtil.getIncludeSystemUserGroup());

		if (Validator.isNotNull(userGroup))
		{
			rolePath = CustomLandingPageUtil.getLandingPageFriendlyURL(userGroup,
					PortalUtil.getCompanyId(request));
		} else
		{
			LOG.debug("No Role assigned to user :  " + currentUser.getFullName());
		}
		return rolePath;
	}

	/**
	 * @param user
	 * @param includeSystemUserGroup
	 * @return
	 */
	private UserGroup getUserUserGroup(final User user, final boolean includeSystemUserGroup)
	{
		UserGroup userUserGroup = null;
		try
		{
			List<UserGroup> userGroups = user.getUserGroups();
			if (Validator.isNotNull(userGroups) && !userGroups.isEmpty())
			{
				// If user is member of more than one UserGroup then it will take
				// first UserGroup from list. If iucludeSystemUserGroup is true then it
				// will also include system userGroups.
				if (includeSystemUserGroup)
				{
					userUserGroup = userGroups.get(0);
				} else
				{
					for (UserGroup userGroup : userGroups)
					{
						if (!PortalUtil.isSystemGroup(userGroup.getName()))
						{
							userUserGroup = userGroup;
							break;
						}
					}
				}
			}
		} catch (SystemException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return userUserGroup;
	}

	private static final Log LOG = LogFactory.getLog(UserGroupPageType.class);
}
