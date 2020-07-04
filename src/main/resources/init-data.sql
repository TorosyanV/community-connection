INSERT INTO `role` (`id`, `name`) VALUES (1, 'ROLE_USER');

INSERT INTO `user` (`id`,`username`,`password_hash`) VALUES (1,'user','$2a$10$IMXArCnx37ek9SAjl32YHuUSxjtVolimLEihDdcOeWTbgf0rnU2bW' ); --password


INSERT INTO `user_role` (`user_id`,`role_id`) VALUES (1,1);

INSERT INTO `event` (id,  created, `date`, deleted, location, `name`, updated, user_id) VALUES (1, '2020-07-04 15:56:59', '2020-07-04 15:51:16', false, 'Some location Address', 'my event', '2020-07-04 15:58:16', 1);