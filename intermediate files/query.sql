create table users(
	user_id int(10) not null unique auto_increment,
	primary key(user_id),
	username varchar(100),
	password varchar(100),
	email varchar(45),
	verification_key varchar(100),
	active boolean default FALSE
);

insert into users(username, password) values("mehul", "710");


create table events(
	event_id int(10) not null unique auto_increment,
	primary key(event_id),
	user_id int(10),
	foreign key(user_id) references users(user_id),
	title varchar(45),
	description varchar(500),
	date datetime,
	creation_datetime datetime,
	last_updated_datetime datetime,
	no_of_times_updated int(10) default 0,
	is_notif_active boolean default FALSE,
	time_before_to_notify int(100) default 0,
	notif_message varchar(100)
);

INSERT INTO events(user_id, title, description, Date, creation_datetime)
VALUES (1, "new tiltle", "new desc", '2020/05/24', '2038-01-19 03:14:07');



create table tokens(
	token varchar(100) not null unique,
	primary key(token),
	user_id int(10),
	foreign key(user_id) references users(user_id),
	issued datetime,
	time_to_live int(100),
	expires datetime
);

INSERT INTO tokens VALUES("abcd", 1, '2020-05-27 09:00:00', 1800, '2020-05-27 11:00:00');