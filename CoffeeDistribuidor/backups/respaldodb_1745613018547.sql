--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: coffee_shops; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coffee_shops (
    shop_id integer NOT NULL,
    shop_name character varying(100) NOT NULL,
    location character varying(200),
    opening_hours character varying(50),
    contact_phone character varying(20)
);


ALTER TABLE public.coffee_shops OWNER TO postgres;

--
-- Name: coffee_shops_shop_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.coffee_shops_shop_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.coffee_shops_shop_id_seq OWNER TO postgres;

--
-- Name: coffee_shops_shop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.coffee_shops_shop_id_seq OWNED BY public.coffee_shops.shop_id;


--
-- Name: coffees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coffees (
    coffee_id integer NOT NULL,
    coffee_name character varying(100) NOT NULL,
    supplier_id integer NOT NULL,
    shop_id integer,
    price numeric(10,2) NOT NULL,
    description text,
    is_active boolean DEFAULT true,
    last_updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT coffees_price_check CHECK ((price > (0)::numeric))
);


ALTER TABLE public.coffees OWNER TO postgres;

--
-- Name: coffees_coffee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.coffees_coffee_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.coffees_coffee_id_seq OWNER TO postgres;

--
-- Name: coffees_coffee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.coffees_coffee_id_seq OWNED BY public.coffees.coffee_id;


--
-- Name: suppliers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suppliers (
    supplier_id integer NOT NULL,
    supplier_name character varying(100) NOT NULL,
    contact_name character varying(100) NOT NULL,
    phone character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    registration_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (((email)::text ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'::text))
);


ALTER TABLE public.suppliers OWNER TO postgres;

--
-- Name: suppliers_supplier_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.suppliers_supplier_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.suppliers_supplier_id_seq OWNER TO postgres;

--
-- Name: suppliers_supplier_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.suppliers_supplier_id_seq OWNED BY public.suppliers.supplier_id;


--
-- Name: coffee_shops shop_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffee_shops ALTER COLUMN shop_id SET DEFAULT nextval('public.coffee_shops_shop_id_seq'::regclass);


--
-- Name: coffees coffee_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffees ALTER COLUMN coffee_id SET DEFAULT nextval('public.coffees_coffee_id_seq'::regclass);


--
-- Name: suppliers supplier_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suppliers ALTER COLUMN supplier_id SET DEFAULT nextval('public.suppliers_supplier_id_seq'::regclass);


--
-- Data for Name: coffee_shops; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coffee_shops (shop_id, shop_name, location, opening_hours, contact_phone) FROM stdin;
1	Café Central	Av. Principal 123	L-V 7:00-22:00	5551112233
3	Arabica Lounge	Plaza Jardín 789	M-D 9:00-23:00	5553334466
4	Mona cafe	Calle revolucion 456	L-S 8:00-20:00	5552557366
\.


--
-- Data for Name: coffees; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coffees (coffee_id, coffee_name, supplier_id, shop_id, price, description, is_active, last_updated) FROM stdin;
1	Colombia Supremo	1	1	45.50	Café suave con notas de caramelo	t	2025-04-25 01:04:27.982137
3	Mexicano Altura	3	3	52.00	Cuerpo balanceado con chocolate	t	2025-04-25 01:04:27.982137
2	Ethiopia Yirgacheffe	2	\N	68.75	Aromas florales con acidez cítrica	t	2025-04-25 01:04:27.982137
5	andatti	2	\N	69.90	cafe negro soluble 	t	2025-04-25 01:04:27.982137
7	Brazil Santos	1	\N	38.90	Sabor intenso con final achocolatado	t	2025-04-25 13:01:15.014556
4	Brazil Santos	1	1	38.90	Sabor intenso con final achocolatado	t	2025-04-25 01:04:27.982137
\.


--
-- Data for Name: suppliers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.suppliers (supplier_id, supplier_name, contact_name, phone, email, registration_date) FROM stdin;
1	Café Premium S.A.	Juan Pérez	5512345678	juan@premiumcoffee.com	2025-04-25 01:04:27.982137
2	Granos Selectos	María García	5523456789	ventas@granosselectos.com	2025-04-25 01:04:27.982137
3	Orgánico del Valle	Carlos Ruiz	5534567890	info@organicovalle.com	2025-04-25 01:04:27.982137
5	nature coffee	jose pereira	66221724807	jose@naturecoffee.com	2025-04-25 12:53:03.806787
\.


--
-- Name: coffee_shops_shop_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.coffee_shops_shop_id_seq', 5, true);


--
-- Name: coffees_coffee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.coffees_coffee_id_seq', 8, true);


--
-- Name: suppliers_supplier_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.suppliers_supplier_id_seq', 5, true);


--
-- Name: coffee_shops coffee_shops_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffee_shops
    ADD CONSTRAINT coffee_shops_pkey PRIMARY KEY (shop_id);


--
-- Name: coffee_shops coffee_shops_shop_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffee_shops
    ADD CONSTRAINT coffee_shops_shop_name_key UNIQUE (shop_name);


--
-- Name: coffees coffees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffees
    ADD CONSTRAINT coffees_pkey PRIMARY KEY (coffee_id);


--
-- Name: suppliers suppliers_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suppliers
    ADD CONSTRAINT suppliers_email_key UNIQUE (email);


--
-- Name: suppliers suppliers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suppliers
    ADD CONSTRAINT suppliers_pkey PRIMARY KEY (supplier_id);


--
-- Name: idx_coffee_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_coffee_name ON public.coffees USING btree (coffee_name);


--
-- Name: idx_shop_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_shop_name ON public.coffee_shops USING btree (shop_name);


--
-- Name: idx_supplier_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_supplier_name ON public.suppliers USING btree (supplier_name);


--
-- Name: coffees fk_shop; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffees
    ADD CONSTRAINT fk_shop FOREIGN KEY (shop_id) REFERENCES public.coffee_shops(shop_id) ON DELETE SET NULL;


--
-- Name: coffees fk_supplier; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coffees
    ADD CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES public.suppliers(supplier_id) ON DELETE RESTRICT;


--
-- Name: TABLE coffee_shops; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.coffee_shops TO developer;


--
-- Name: SEQUENCE coffee_shops_shop_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.coffee_shops_shop_id_seq TO developer;


--
-- Name: TABLE coffees; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.coffees TO developer;


--
-- Name: SEQUENCE coffees_coffee_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.coffees_coffee_id_seq TO developer;


--
-- Name: TABLE suppliers; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.suppliers TO developer;


--
-- Name: SEQUENCE suppliers_supplier_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.suppliers_supplier_id_seq TO developer;


--
-- PostgreSQL database dump complete
--

