INSERT INTO public.users(
	username, password, email, rank, enabled)
	VALUES ('admin', '$2a$04$nFSEioczBFIqwrdgN0XDo.4QFNqXW/QVNcmmkIAdNxIRYzYtJVJDe', 'admin', 'elite', true);
INSERT INTO public.authorities(
	username, authority)
	VALUES ('admin', 'ROLE_ADMIN');


INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Test for demo', 'test to demonstrate our awesome webapp', '64cf0478bbdf4a83a745894b3994fe40', 0, 5, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 2 basics', 'more nagyon kígyó', '134cr23rd32dr2dr2drd23r', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 2.1 basics', 'more nagyon kígyó', 'r23r3ccrc2tcjmrj', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 3.1 basics', 'more nagyon kígyó', 'm923cej92jme293je92', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 3.5 basics', 'more nagyon kígyó', 'cmijjeco3o4o2423434', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 3.7 basics', 'more nagyon kígyó', 'c3r3r2ra2r323r2r23r3r', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 3.2 basics', 'more nagyon kígyó', 'r23crr23crrcr23cr23cr3r23', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 2.6 basics', 'more nagyon kígyó', 'c3rr3rcr2323r34rfft34', 5, 10, 1, true, 'language', false);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 3.4 basics', 'more nagyon kígyó', 't43ct43t34trcfvwrwrvw', 5, 10, 1, true, 'language', true);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 2.8 basics', 'more nagyon kígyó', '32v35h5hrwdf2cr3r23r23r', 5, 10, 1, true, 'language', true);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('Python 2.9 basics', 'more nagyon kígyó', 'r23v3443rnutrbenzsese', 5, 10, 1, true, 'language', true);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('.net basics', 'c fence', 'wqceqr12r3r2r', 5, 10, 1, true, 'language', true);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('java basics', 'asd', 'c23cr3r23rc23cr23r', 5, 10, 1, true, 'language', true);
INSERT INTO public.tests(
	title, description, path, price, max_points, creator, enabled, type,live)
	VALUES ('javascript basics', 'asdsafasf', '23cr23r23ctc3ta', 5, 10, 1, true, 'language', true);
