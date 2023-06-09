CREATE TABLE IF NOT EXISTS projects (
      _id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      "projectName" VARCHAR(255) NOT NULL,
      organization VARCHAR(255) NOT NULL,
      "managerId" VARCHAR(255) NOT NULL,
      "createdAt" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      "updatedAt" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );