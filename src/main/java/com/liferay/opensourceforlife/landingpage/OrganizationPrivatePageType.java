/**
 * Returns custom landing path for organization's Private default page which user belongs to
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
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class OrganizationPrivatePageType implements LandingPageType
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
		String organizationPath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);
		List<Organization> userOrganizations = currentUser.getOrganizations();

		if (Validator.isNotNull(userOrganizations) && !userOrganizations.isEmpty())
		{
			// If user is member of more than one organization then it will take
			// first organization from list
			Organization organization = userOrganizations.get(0);
			if (Validator.isNotNull(organization))
			{
				Group organizationGroup = organization.getGroup();
				if (organizationGroup.getPrivateLayoutsPageCount() > 0)
				{
					organizationPath = CustomLandingPageUtil.getGroupFriendlyURL(request,
							organizationGroup, Boolean.TRUE, Boolean.FALSE)
							+ CustomLandingPageUtil.getLandingPageFriendlyURL(organizationGroup,organization.getExpandoBridge(),
									PortalUtil.getCompanyId(request), Boolean.TRUE);
				} else
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug(organizationGroup.getName()
								+ " organization site doesn't have any private page. So default landing page will be used");
					}
				}
			}
		}
		return organizationPath;
	}

	private static final Log LOG = LogFactory.getLog(OrganizationPrivatePageType.class);
}
