package br.com.concrete.tentacle.mock

import br.com.concrete.tentacle.data.models.*
import okhttp3.MediaType
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.HttpException
import retrofit2.Response

private val messageSuccess = listOf("success")

/**
 * COMMON
 */
val errorWithMessage = Throwable("Error",
    HttpException(
        Response.error<HttpException>(400,
            ResponseBody.create(MediaType.parse("application/json"),
                "{" +
                            "message: [\"Error Message 01\", \"Error Message 02\"]" +
                        "}")
        ))
)

val error401 = Throwable("Error 401",
    HttpException(
        Response.error<HttpException>(401,
            ResponseBody.create(MediaType.parse("text/plain"), "")
        ))
)

/**
 * MOCK USED FOR STATES
 */
private val states = StateResponse(listOf(
    State(
        _id = "5a107a681592ca19f7b4a851",
        initials = "AC",
        name = "Acre"
    ),
    State(
        _id = "5a107a681592ca19f7b4a852",
        initials = "AL",
        name = "Alagoas"
    ),
    State(
        _id = "5a107a681592ca19f7b4a854",
        initials = "AP",
        name = "Amapá"
    ),
    State(
        _id = "5a107a681592ca19f7b4a853",
        initials = "AM",
        name = "Amazonas"
    ),
    State(
        _id = "5a107a681592ca19f7b4a855",
        initials = "BA",
        name = "Bahia"
    ),
    State(
        _id = "5a107a681592ca19f7b4a856",
        initials = "CE",
        name = "Ceará"
    ),
    State(
        _id = "5a107a681592ca19f7b4a857",
        initials = "DF",
        name = "Distrito Federal"
    ),
    State(
        _id = "5a107a681592ca19f7b4a858",
        initials = "ES",
        name = "Espírito Santo"
    ),
    State(
        _id = "5a107a681592ca19f7b4a859",
        initials = "GO",
        name = "Goiás"
    ),
    State(
        _id = "5a107a681592ca19f7b4a85a",
        initials = "MA",
        name = "Maranhão"
    ),
    State(
        _id = "5a107a691592ca19f7b4a85d",
        initials = "MT",
        name = "Mato Grosso"
    ),
    State(
        _id = "5a107a691592ca19f7b4a85c",
        initials = "MS",
        name = "Mato Grosso do Sul"
    ),
    State(
        _id = "5a107a681592ca19f7b4a85b",
        initials = "MG",
        name = "Minas Gerais"
    ),
    State(
        _id = "5a107a691592ca19f7b4a862",
        initials = "PR",
        name = "Paraná"
    ),
    State(
        _id = "5a107a691592ca19f7b4a85f",
        initials = "PB",
        name = "Paraíba"
    ),
    State(
        _id = "5a107a691592ca19f7b4a85e",
        initials = "PA",
        name = "Pará"
    ),
    State(
        _id = "5a107a691592ca19f7b4a860",
        initials = "PE",
        name = "Pernambuco"
    ),
    State(
        _id = "5a107a691592ca19f7b4a861",
        initials = "PI",
        name = "Piauí"
    ),
    State(
        _id = "5a107a691592ca19f7b4a864",
        initials = "RN",
        name = "Rio Grande do Norte"
    ),
    State(
        _id = "5a107a691592ca19f7b4a867",
        initials = "RS",
        name = "Rio Grande do Sul"
    ),
    State(
        _id = "5a107a691592ca19f7b4a863",
        initials = "RJ",
        name = "Rio de Janeiro"
    ),
    State(
        _id = "5a107a691592ca19f7b4a865",
        initials = "RO",
        name = "Rondônia"
    ),
    State(
        _id = "5a107a691592ca19f7b4a866",
        initials = "RR",
        name = "Roraima"
    ),
    State(
        _id = "5a107a691592ca19f7b4a868",
        initials = "SC",
        name = "Santa Catarina"
    ),
    State(
        _id = "5a107a691592ca19f7b4a869",
        initials = "SE",
        name = "Sergipe"
    ),
    State(
        _id = "5a107a691592ca19f7b4a86a",
        initials = "SP",
        name = "São Paulo"
    ),
    State(
        _id = "5a107a691592ca19f7b4a86b",
        initials = "TO",
        name = "Tocantins"
    )
))
val baseModelStateSuccess = BaseModel(messageSuccess, states)

/**
 * MOCK USED FOR CITIES
 */
private val cities = listOf("Abreu e Lima",
    "Afogados da Ingazeira",
    "Afrânio",
    "Agrestina",
    "Água Preta",
    "Águas Belas",
    "Alagoinha",
    "Aliança",
    "Altinho",
    "Amaraji",
    "Angelim",
    "Araçoiaba",
    "Araripina",
    "Arcoverde",
    "Barra de Guabiraba",
    "Barreiros",
    "Belém de Maria",
    "Belém de São Francisco",
    "Belo Jardim",
    "Betânia",
    "Bezerros",
    "Bodocó",
    "Bom Conselho",
    "Bom Jardim",
    "Bonito",
    "Brejão",
    "Brejinho",
    "Brejo da Madre de Deus",
    "Buenos Aires",
    "Buíque",
    "Cabo de Santo Agostinho",
    "Cabrobó",
    "Cachoeirinha",
    "Caetés",
    "Calçado",
    "Calumbi",
    "Camaragibe",
    "Camocim de São Félix",
    "Camutanga",
    "Canhotinho",
    "Capoeiras",
    "Carnaíba",
    "Carnaubeira da Penha",
    "Carpina",
    "Caruaru",
    "Casinhas",
    "Catende",
    "Cedro",
    "Chã de Alegria",
    "Chã Grande",
    "Condado",
    "Correntes",
    "Cortês",
    "Cumaru",
    "Cupira",
    "Custódia",
    "Dormentes",
    "Escada",
    "Exu",
    "Feira Nova",
    "Fernando de Noronha",
    "Ferreiros",
    "Flores",
    "Floresta",
    "Frei Miguelinho",
    "Gameleira",
    "Garanhuns",
    "Glória do Goitá",
    "Goiana",
    "Granito",
    "Gravatá",
    "Iati",
    "Ibimirim",
    "Ibirajuba",
    "Igarassu",
    "Iguaraci",
    "Inajá",
    "Ingazeira",
    "Ipojuca",
    "Ipubi",
    "Itacuruba",
    "Itaíba",
    "Itamaracá",
    "Itambé",
    "Itapetim",
    "Itapissuma",
    "Itaquitinga",
    "Jaboatão dos Guararapes",
    "Jaqueira",
    "Jataúba",
    "Jatobá",
    "João Alfredo",
    "Joaquim Nabuco",
    "Jucati",
    "Jupi",
    "Jurema",
    "Lagoa do Carro",
    "Lagoa do Itaenga",
    "Lagoa do Ouro",
    "Lagoa dos Gatos",
    "Lagoa Grande",
    "Lajedo",
    "Limoeiro",
    "Macaparana",
    "Machados",
    "Manari",
    "Maraial",
    "Mirandiba",
    "Moreilândia",
    "Moreno",
    "Nazaré da Mata",
    "Olinda",
    "Orobó",
    "Orocó",
    "Ouricuri",
    "Palmares",
    "Palmeirina",
    "Panelas",
    "Paranatama",
    "Parnamirim",
    "Passira",
    "Paudalho",
    "Paulista",
    "Pedra",
    "Pesqueira",
    "Petrolândia",
    "Petrolina",
    "Poção",
    "Pombos",
    "Primavera",
    "Quipapá",
    "Quixaba",
    "Recife",
    "Riacho das Almas",
    "Ribeirão",
    "Rio Formoso",
    "Sairé",
    "Salgadinho",
    "Salgueiro",
    "Saloá",
    "Sanharó",
    "Santa Cruz",
    "Santa Cruz da Baixa Verde",
    "Santa Cruz do Capibaribe",
    "Santa Filomena",
    "Santa Maria da Boa Vista",
    "Santa Maria do Cambucá",
    "Santa Terezinha",
    "São Benedito do Sul",
    "São Bento do Una",
    "São Caitano",
    "São João",
    "São Joaquim do Monte",
    "São José da Coroa Grande",
    "São José do Belmonte",
    "São José do Egito",
    "São Lourenço da Mata",
    "São Vicente Ferrer",
    "Serra Talhada",
    "Serrita",
    "Sertânia",
    "Sirinhaém",
    "Solidão",
    "Surubim",
    "Tabira",
    "Tacaimbó",
    "Tacaratu",
    "Tamandaré",
    "Taquaritinga do Norte",
    "Terezinha",
    "Terra Nova",
    "Timbaúba",
    "Toritama",
    "Tracunhaém",
    "Trindade",
    "Triunfo",
    "Tupanatinga",
    "Tuparetama",
    "Venturosa",
    "Verdejante",
    "Vertente do Lério",
    "Vertentes",
    "Vicência",
    "Vitória de Santo Antão",
    "Xexéu")
val requestedState = states.list[16]._id
val baseModelCitiesSuccess = BaseModel(messageSuccess, CityResponse(cities, requestedState))

/**
 * MOCK USED FOR USER
 */
private val user = User(
    _id = "1",
    name = "daivid",
    email = "daivid@gmail.com",
    phone = "99 123456789",
    password = "123456",
    state = State("hash_code", "PE", "Pernambuco"),
    city = "Recife",
    createdAt = "today",
    updatedAt = "today"
)
val userRequest = UserRequest(
    name = "daivid",
    email = "daivid@gmail.com",
    phone = "99 123456789",
    password = "123456",
    state = "hash_code",
    city = "Recife"
)
val baseModelUserSuccess = BaseModel(messageSuccess, user)

/**
 * this block is to mock the apiService result
 * in order to verify and make a unit test for the repository
 */
val session = Session(accessToken = "ACCESS_TOKEN",
    refreshToken = "REFRESH_TOKEN",
    tokenType = "TOKEN_TYPE")
val message = listOf(
    "LOGGIN SUCCESS"
)
val baseModelLoginSuccess = BaseModel(message, session)

/**
 * Mock for shared preferences
 */
val stringKey = "KEY"
val string = "STRING"

val sessionKey = "sessionKey"
val sessionForPreference = session

val stringExpectedWhenThereIsNoOne = ""

/**
 * Mock for Specific Wrong responses
 */
val mockWebServer = MockWebServer()
val unauthorized = MockResponse()
    .setResponseCode(401)
    .addHeader("access-control-allow-origin","*")
    .setBody("")

