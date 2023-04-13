CREATE TABLE IF NOT EXISTS tasks (
      _id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      "taskName" VARCHAR(255) NOT NULL,
      "coordinatorId" VARCHAR(255) NOT NULL,
      epic VARCHAR(255) NOT NULL,
      "columnId" VARCHAR(255) NOT NULL,
      note TEXT NOT NULL,
      type VARCHAR(255) NOT NULL,
      "projectId" VARCHAR(255) NOT NULL,
      "storyPoints" INTEGER NOT NULL,
      index INTEGER NOT NULL,
      "createdAt" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      "updatedAt" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );