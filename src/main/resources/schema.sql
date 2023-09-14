create table if not exists user (
    id bigint primary key not null,
    login varchar(255) not null,
    name varchar(255) not null,
    profile_url varchar(255),
    email varchar(255)
);

create table if not exists repository (
    id int auto_increment primary key,
    user_id bigint,
    repo_name varchar(255) not null,
    repo_url varchar(255) not null,
    owner_login varchar(255) not null,
    foreign key (user_id) references user (id)
);

create table if not exists credential (
    id int auto_increment primary key,
    user_id bigint,
    aws_account_id varchar(255) not null,
    access_key varchar(255) not null,
    secret_key varchar(255) not null,
    region varchar(255) not null,
    github_oauth_token varchar(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (user_id) references user (id)
);

create table if not exists authentication (
    id int auto_increment primary key,
    user_id bigint,
    access_token varchar(255) not null,
    refresh_token varchar(255) not null,
    grant_type varchar(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (user_id) references user (id)
);

create table if not exists service (
    id int auto_increment primary key,
    user_id bigint,
    stack_name varchar(255) not null,
    stack_type varchar(255) not null,
    end_point varchar(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (user_id) references user (id)
);

create table if not exists post (
    post_id int auto_increment primary key,
    user_id bigint,
    title varchar(255) not null,
    content varchar(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (user_id) references user (id)
);

create table if not exists tag (
    tag_id int auto_increment primary key,
    tag_name varchar(255) not null
);

create table if not exists post_tag_association (
    post_tag_association_id int auto_increment primary key,
    post_id int,
    tag_id int,
    foreign key (post_id) references post (post_id),
    foreign key (tag_id) references tag (tag_id)
);

create table if not exists comment (
    comment_id int auto_increment primary key,
    post_id int,
    user_id bigint,
    content varchar(255) not null,
    create_at timestamp not null,
    update_at timestamp not null,
    foreign key (post_id) references post (post_id),
    foreign key (user_id) references user (id)
);

create table if not exists authorities (
    authority_id int auto_increment primary key,
    authority_name varchar(255) not null
);

create table if not exists apollo_user_token (
    user_for_token_id varchar(255) primary key not null,
    user_id varchar(255) not null,
    activated boolean not null
);

create table if not exists user_authority (
    id int auto_increment primary key,
    authority_id int,
    user_for_token_id varchar(255),
    foreign key (authority_id) references authorities (authority_id),
    foreign key (user_for_token_id) references apollo_user_token (user_for_token_id)
);













