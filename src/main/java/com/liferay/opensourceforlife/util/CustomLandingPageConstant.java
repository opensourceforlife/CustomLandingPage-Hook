/**
 * 
 */

package com.liferay.opensourceforlife.util;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public final class CustomLandingPageConstant
{

	/**
	 * 
	 */
	private CustomLandingPageConstant()
	{
	}

	public static final String OVERRIDE_DEFAULT_LANDING_PAGE_PATH = "override.default.landing.page.path";
	public static final String CUSTOM_LANDING_PAGE_TYPE = "custom.landing.page.type";
	public static final String INCLUDE_LANGUAGE = "include.language";

	public static final String USER_PRIVATE_PAGE = "userPrivatePage";
	public static final String USER_PUBLIC_PAGE = "userPublicPage";
	public static final String SITE_PUBLIC_PAGE = "sitePublicPage";
	public static final String SITE_PRIVATE_PAGE = "sitePrivatePage";
	public static final String ORGANIZATION_PRIVATE_PAGE = "organizationPrivatePage";
	public static final String ORGANIZATION_PUBLIC_PAGE = "organizationPublicPage";
	public static final String DEFAULT_LANDING_PAGE_TYPE = "default";

	public static final String GUEST_GROUP_FRIENDLY_URL = "/guest";
	public static final String PUBLIC = "Public";
	public static final String PRIVATE = "Private";
	public static final int FIVE = 5;
	public static final int FOUR = 4;

	public static final String LANDING_PAGE_KEY_DEFAULT_VALUE = "landingPage";
	public static final String CUSTOM_LANDING_PAGE_KEY = "custom.landing.page.key";

	public static final String PRIVATE_USER_SERVLET_MAPPING = PropsUtil
			.get(PropsKeys.LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING);

	public static final String PRIVATE_GROUP_SERVLET_MAPPING = PropsUtil
			.get(PropsKeys.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING);

	public static final String PUBLIC_GROUP_SERVLET_MAPPING = PropsUtil
			.get(PropsKeys.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING);

	public static final String PORTAL_CONTEXT = PortalUtil.getPathContext();
}
