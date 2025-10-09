create table CuscalToC_TermsOfUse (
	entryId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	screenName VARCHAR(75) null,
	version DOUBLE,
	acceptedDate DATE null,
	createDate DATE null,
	modifiedDate DATE null
);