create table file
(
    fileId        bigint(20) not null auto_increment,
    fileSize      bigint(10),
    fileUrl       varchar(500),
    identifier    varchar(32),
    isOSS         int(1),
    pointCount    int(11),
    storageType   int(1),
    timeStampName varchar(500),
    primary key (fileId)
) engine=InnoDB;
create table operation_log
(
    operationLogId bigint not null auto_increment,
    detail         varchar(255),
    logLevel       varchar(255),
    operation      varchar(255),
    operationObj   varchar(255),
    result         varchar(255),
    source         varchar(255),
    terminal       varchar(255),
    time           varchar(255),
    userId         bigint not null,
    primary key (operationLogId)
) engine=InnoDB;
create table permission
(
    permissionId bigint not null auto_increment,
    available    bit,
    name         varchar(255),
    parentId     bigint,
    parentIds    varchar(255),
    permission   varchar(255),
    resourceType varchar(255),
    url          varchar(255),
    primary key (permissionId)
) engine=InnoDB;
create table role
(
    roleId      bigint not null auto_increment,
    available   bit,
    description varchar(255),
    role        varchar(255),
    primary key (roleId)
) engine=InnoDB;
create table role_permission
(
    roleId       bigint not null,
    permissionId bigint not null
) engine=InnoDB;
create table share
(
    shareId        bigint not null auto_increment,
    endTime        varchar(255),
    extractionCode varchar(255),
    shareBatchNum  varchar(255),
    shareStatus    integer,
    shareTime      varchar(255),
    shareType      integer,
    userId         bigint,
    primary key (shareId)
) engine=InnoDB;
create table share_file
(
    shareFileId   bigint not null auto_increment,
    shareBatchNum varchar(255),
    shareFilePath varchar(255),
    userFileId    bigint,
    primary key (shareFileId)
) engine=InnoDB;
create table storage
(
    storageId   bigint(20) not null auto_increment,
    storageSize bigint(20),
    userId      bigint(20),
    primary key (storageId)
) engine=InnoDB;
create table user
(
    userId       bigint(20) not null auto_increment,
    birthday     varchar(30),
    email        varchar(100),
    imageUrl     varchar(100),
    password     varchar(35),
    registerTime varchar(30),
    salt         varchar(20),
    sex          varchar(3),
    telephone    varchar(15),
    username     varchar(30),
    primary key (userId)
) engine=InnoDB;
create table user_role
(
    userId bigint(20) not null,
    roleId bigint     not null
) engine=InnoDB;
create table user_file
(
    userFileId     bigint(20) not null auto_increment,
    deleteBatchNum varchar(50),
    deleteFlag     int(11),
    deleteTime     varchar(25),
    extendName     varchar(100),
    fileId         bigint(20),
    fileName       varchar(100),
    filePath       varchar(500),
    isDir          int(1),
    uploadTime     varchar(25),
    userId         bigint(20),
    primary key (userFileId)
) engine=InnoDB;
alter table user_file add constraint fileindex unique (fileName, filePath, extendName, deleteFlag, userId);
alter table role_permission add constraint FKmsjtuo1smqbduu6wt9gekj7k6 foreign key (permissionId) references permission (permissionId);
alter table role_permission add constraint FKsrw6jhwxy1l8i8urr987m0byj foreign key (roleId) references role (roleId);
alter table user_role add constraint FKbo5ik0bthje7hum554xb17ry6 foreign key (roleid) references role (roleId);
alter table user_role add constraint FKj5g46wgmq1wmqfhv78g7cxaqe foreign key (userId) references user (userId);