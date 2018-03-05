using Backend.Data.Database;
using Backend.Data.Repositories;
using Backend.Domain;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Swashbuckle.AspNetCore.Swagger;

namespace Backend
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services
                .AddMvc()
                .AddJsonOptions(options =>
                {
                    var serializerSettings = options.SerializerSettings;
                    
                    serializerSettings.NullValueHandling = NullValueHandling.Ignore;
                    serializerSettings.Converters.Add(new StringEnumConverter(true));
                });

            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new Info {Title = "KhSM API", Version = "v1"});
            });

            ConfigDataServices(services);
        }

        public static void ConfigDataServices(IServiceCollection services)
        {
            services.AddScoped<DatabaseContext>();
            
            services.AddScoped<MeetingsRepository>();
            services.AddScoped<DisciplinesRepository>();
            services.AddScoped<ResultsRepository>();
            services.AddScoped<UserRepository>();
            services.AddScoped<SessionRepository>();

            services.AddScoped<MeetingsManager>();
            services.AddScoped<DisciplinesManager>();
            services.AddScoped<ResultsManager>();
            services.AddScoped<UsersManager>();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseSwagger();
            app.UseSwaggerUI(c =>
            {
                c.SwaggerEndpoint("/swagger/v1/swagger.json", "KhSM API v1");
            });

            app.UseMvc();
        }
    }
}