<header id="header">
	<div class="header-banner-box">
		<div class="navbar navbar-classic navbar-top py-0">
			<div class="container-fluid container-fluid-max-xl user-personal-bar">
				<div class="align-items-center d-flex justify-content-between w-100">
					
					<div class="site-logo-fragment">
						<a  href="${site_default_url}" >
							<#assign siteLogo = htmlUtil.escape(themeDisplay.getCompanyLogo()) />
							<img alt="${logo_description}" class="mr-2" height="100" src="${site_logo}" />
						</a>
					</div>
					

					<div class="autofit-col banner-top d-sm-none d-none d-md-block">
						<img class="img-fluid" src="${themeDisplay.getPathThemeImages()}/common/image_gallery.gif" />
					</div>

					<div class="autofit-col">
						<div class="my-acc-container align-items-end d-flex justify-content">
							<#if themeDisplay.isSignedIn()>
							<div class="autofit-col my-account mr-4">
									<div><strong>Logged In As</strong></div>
									'${user.getDisplayEmailAddress()}'
									<a href="/web/cuscal/manage-your-account"><strong>Manage Your Account</strong></a>
							</div>
							
							<div class="user-personal-bar-fragment">
									<a href="/c/portal/logout" class="btn btn-primary"><strong>Log out</strong></a>
							</div>
							<#else>
							<div class="autofit-col" id="contact-us">
									<a href="/c/portal/login" id="customer-login" >Customer Login</a>
									<div><strong>Contact Us</strong></div>
									<div>1300 650 501</div>
									<a href="mailto:info@cuscal.com.au" id="email" >info@cuscal.com.au</a>
							</div>
							</#if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="public-navigation-bar" id="public-navigation-bar">
		<#include "${full_templates_path}/navigation.ftl" />					
	</div>	
</header>