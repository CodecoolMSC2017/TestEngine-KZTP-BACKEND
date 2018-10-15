INSERT INTO public.users(
	username, password, email, rank, enabled)
	VALUES ('admin', '$2a$04$nFSEioczBFIqwrdgN0XDo.4QFNqXW/QVNcmmkIAdNxIRYzYtJVJDe', 'admin', 'elite', true);
INSERT INTO public.authorities(
	username, authority)
	VALUES ('admin', 'ROLE_ADMIN');