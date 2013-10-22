/**
 * Returns custom landing path for first site which user belongs to
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
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class SitePrivatePageType implements LandingPageType
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
		String sitePath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);
		List<Group> userSites = CustomLandingPageUtil.getSites(currentUser.getUserId());

		if (Validator.isNotNull(userSites) && !userSites.isEmpty())
		{
			// If user is member of more than one sites then it will take
			// first site from list
			Group site = userSites.get(0);
			if (site.getPrivateLayoutsPageCount() > 0)
			{
				sitePath = CustomLandingPageUtil.getGroupFriendlyURL(request, site, Boolean.TRUE,
						Boolean.FALSE)
						+ CustomLandingPageUtil.getLandingPageFriendlyURL(site,
								PortalUtil.getCompanyId(request), Boolean.TRUE);
			} else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(site.getName()
							+ " site doesn't have any private page. So default landing page will be used");
				}
			}
		}
		return sitePath;
	}

	private static final Log LOG = LogFactory.getLog(SitePrivatePageType.class);
}
